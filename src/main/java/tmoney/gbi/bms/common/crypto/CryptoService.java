package tmoney.gbi.bms.common.crypto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tmoney.gbi.bms.common.annotation.Encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class CryptoService {

    private static final String secretKey = "a1b2c3d4e5f6g7h8a1b2c3d4e5f6g7h8"; // 32바이트 (256비트) 키

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    // ✨ 리팩토링 포인트 1: 클래스별 암호화 필드 캐시
    private final Map<Class<?>, List<Field>> encryptableFieldCache = new ConcurrentHashMap<>();

    public String encrypt(String value) {
        if (value == null || value.isEmpty()) {
            return ""; // 빈 값은 암호화하지 않음
        }
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedBytes = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt data", e);
        }
    }

    public String decrypt(String encryptedValue) {
        if (encryptedValue == null || encryptedValue.isEmpty()) {
            return ""; // 빈 값은 복호화하지 않음
        }
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedValue);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt data", e);
        }
    }

    public void encryptFields(Object object){
        if (object == null) { return;}
        // 캐시에서 암호화할 필드 목록을 가져옵니다.
        List<Field> fieldsToEncrypt = getEncryptableFields(object.getClass());

        // 필드를 순회하며 암호화를 적용합니다.
        for (Field field : fieldsToEncrypt) {
            try {
                String originalValue = (String) field.get(object);
                if (originalValue != null) { // null 값은 암호화하지 않음
                    String encryptedValue = encrypt(originalValue);
                    field.set(object, encryptedValue);
                }
            } catch (IllegalAccessException e) {
                log.error("Security exception. Cannot access field '{}'.", field.getName(), e);
            }
        }
    }

    /**
     * ✨ 리팩토링 포인트 3: 캐싱 로직 구현
     * 클래스 정보를 기반으로 암호화가 필요한 필드를 찾아 캐시에 저장하거나,
     * 캐시된 정보를 반환합니다.
     */
    private List<Field> getEncryptableFields(Class<?> clazz) {
        return encryptableFieldCache.computeIfAbsent(clazz, key -> {
            log.debug("Caching encryptable fields for class: {}", key.getSimpleName());
            return Stream.of(key.getDeclaredFields()) // 클래스의 모든 필드를 스트림으로 변환
                    .filter(field -> field.isAnnotationPresent(Encrypt.class)) // @Encrypt 어노테이션이 있는 필드만 필터링
                    .peek(field -> {
                        if (!field.getType().equals(String.class)) {
                            throw new IllegalArgumentException("@Encrypt annotation can only be applied to String fields. Error in field: " + field.getName());
                        }
                        field.setAccessible(true); // private 필드에 접근 가능하도록 설정 (성능 향상)
                    })
                    .collect(Collectors.toList());
        });
    }
}