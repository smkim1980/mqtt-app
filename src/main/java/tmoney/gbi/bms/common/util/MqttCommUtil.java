package tmoney.gbi.bms.common.util;

import com.google.protobuf.Timestamp;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tmoney.gbi.bms.converter.MessageConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MqttCommUtil {

    public static String toLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        ZoneId seoulZone = ZoneId.of("Asia/Seoul");
        // 1. Timestamp를 Instant로 변환
        Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());

        return toDateTimeFormatter(LocalDateTime.ofInstant(instant, seoulZone));
    }

    public static String toDateTimeFormatter(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        // LocalDateTime을 원하는 형식으로 포맷팅
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static MessageConverter<?> getMessageConverter(String topic , List<MessageConverter<?>> messageConverters) {
        // 이 프로세서가 지원하는 토픽을 처리할 수 있는 컨버터를 찾음
        return messageConverters.stream()
                .filter(c -> c.canHandle(topic))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No MessageConverter found for topic: " + topic));

    }

}
