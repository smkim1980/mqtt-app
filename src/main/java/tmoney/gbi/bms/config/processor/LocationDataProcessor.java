package tmoney.gbi.bms.config.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tmoney.gbi.bms.common.converter.LocationMessageConverter;
import tmoney.gbi.bms.common.crypto.CryptoService;
import tmoney.gbi.bms.mapper.CommonInsertMapper;
import tmoney.gbi.bms.model.EncryptedLocationDto;

import java.util.List;

/**
 * 위치 정보(EncryptedLocationDto) 처리를 위한 구체적인 전략 클래스.
 */
@Component
@RequiredArgsConstructor
public class LocationDataProcessor implements DataProcessor<EncryptedLocationDto> {

    private final LocationMessageConverter locationConverter;
    private final CryptoService cryptoService;
    private final CommonInsertMapper commonInsertMapper;

    @Override
    public boolean supports(String topicPrefix) {
        // 현재는 모든 토픽에 대해 Location 처리를 한다고 가정합니다.
        // 향후 다른 DTO가 추가되면, 토픽에 따라 분기하는 로직이 필요합니다.
        // 예를 들어, topicPrefix.startsWith("obe") || topicPrefix.startsWith("bit") 등으로 구분할 수 있습니다.
        return true;
    }

    @Override
    public EncryptedLocationDto convert(byte[] message) throws Exception {
        return locationConverter.convert(message);
    }

    @Override
    public void process(EncryptedLocationDto dto) {
        cryptoService.encryptFields(dto);
    }

    @Override
    public void batchInsert(List<EncryptedLocationDto> batch) {
        commonInsertMapper.insertLocationBatch(batch);
    }
}
