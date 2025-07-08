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
    public boolean supports(String topic) {
        // "obe" 또는 "bit"로 시작하는 토픽을 지원합니다.
        return topic.startsWith(tmoney.gbi.bms.common.constant.TopicRuleNames.InfoType.OBE) || 
               topic.startsWith(tmoney.gbi.bms.common.constant.TopicRuleNames.InfoType.BIT);
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
