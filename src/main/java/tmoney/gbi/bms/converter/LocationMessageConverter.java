package tmoney.gbi.bms.converter;

import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.stereotype.Component;
import tmoney.gbi.bms.model.EncryptedLocationDto;
import tmoney.gbi.bms.proto.Location;

import java.time.Instant;

import static tmoney.gbi.bms.common.constant.MqttTopic.OBE_TBUS_INB;

@Component
public class LocationMessageConverter implements MessageConverter<EncryptedLocationDto> {

    @Override
    public EncryptedLocationDto convert(byte[] message) throws InvalidProtocolBufferException {
        Location locationProto = Location.parseFrom(message);

        EncryptedLocationDto dto = new EncryptedLocationDto();
        dto.setDeviceId("device-from-proto"); // 예시
        dto.setVehicleId(locationProto.hasLastStopId() ? locationProto.getLastStopId().getValue() : "unknown-vehicle"); // 예시
        dto.setEncryptedLatitude(String.valueOf(locationProto.getLatitude()));
        dto.setEncryptedLongitude(String.valueOf(locationProto.getLongitude()));
        dto.setOccurDt(Instant.ofEpochSecond(locationProto.getOccurAt().getSeconds(), locationProto.getOccurAt().getNanos()).toEpochMilli());

        return dto;
    }

    @Override
    public boolean canHandle(String topic) {
        return topic != null && topic.equals(OBE_TBUS_INB.getTopicString());
    }
}