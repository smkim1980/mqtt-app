package tmoney.gbi.bms.converter.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tmoney.gbi.bms.common.crypto.CryptoService;
import tmoney.gbi.bms.converter.MessageConverter;
import tmoney.gbi.bms.model.EncryptedLocationDto;
import tmoney.gbi.bms.proto.Location;

import java.time.Instant;

import static tmoney.gbi.bms.common.constant.MqttTopic.OBE_TBUS_INB;

@Component
@RequiredArgsConstructor
public class LocationMessageConverter implements MessageConverter<EncryptedLocationDto> {

    private final CryptoService cryptoService;

    @Override
    public EncryptedLocationDto convert(byte[] message) throws InvalidProtocolBufferException {
        Location locationProto = Location.parseFrom(message);

        EncryptedLocationDto dto = new EncryptedLocationDto();
        dto.setDeviceId("device-from-proto"); // 예시
        dto.setVehicleId(locationProto.hasLastStopId() ? locationProto.getLastStopId().getValue() : "unknown-vehicle"); // 예시
        dto.setEncryptedLatitude(String.valueOf(locationProto.getLatitude()));
        dto.setEncryptedLongitude(String.valueOf(locationProto.getLongitude()));
        dto.setOccurDt(Instant.ofEpochSecond(locationProto.getOccurAt().getSeconds(), locationProto.getOccurAt().getNanos()).toEpochMilli());
        return encrypt(dto);
    }

    @Override
    public boolean canHandle(String topic) {
        return topic != null && topic.equals(OBE_TBUS_INB.getTopicString());
    }

    @Override
    public EncryptedLocationDto encrypt(EncryptedLocationDto dto) {
        return cryptoService.encryptFields(dto);
    }


}