package tmoney.gbi.bms.processor.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tmoney.gbi.bms.common.constant.MqttTopic;
import tmoney.gbi.bms.converter.MessageConverter;
import tmoney.gbi.bms.common.crypto.CryptoService;
import tmoney.gbi.bms.mapper.CommonInsertMapper;
import tmoney.gbi.bms.model.EncryptedLocationDto;
import tmoney.gbi.bms.processor.DataProcessor;

import java.util.List;

/**
 * 위치 정보(EncryptedLocationDto) 처리를 위한 구체적인 전략 클래스.
 */
@Component
@RequiredArgsConstructor
public class LocationDataProcessor implements DataProcessor<EncryptedLocationDto> {

    private final CryptoService cryptoService;
    private final CommonInsertMapper commonInsertMapper;
    private final List<MessageConverter<?>> messageConverters;

    private static final String SUPPORTED_TOPIC = MqttTopic.OBE_TBUS_INB.getTopicString();

    @Override
    public boolean supports(String topic) {
        // 이 프로세서는 정확히 일치하는 토픽만 지원합니다.
        return SUPPORTED_TOPIC.equals(topic);
    }

    @Override
    @SuppressWarnings("unchecked")
    public EncryptedLocationDto convert(byte[] message) throws Exception {
        // 이 프로세서가 지원하는 토픽을 처리할 수 있는 컨버터를 찾음
        MessageConverter<EncryptedLocationDto> converter = (MessageConverter<EncryptedLocationDto>) messageConverters.stream()
                .filter(c -> c.canHandle(SUPPORTED_TOPIC))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No MessageConverter found for topic: " + SUPPORTED_TOPIC));

        return converter.convert(message);
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
