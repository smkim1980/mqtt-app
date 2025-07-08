package tmoney.gbi.bms.common.converter;

import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.stereotype.Component;
import tmoney.gbi.bms.model.EncryptedLocationDto;
import tmoney.gbi.bms.proto.Location;

import java.time.Instant;

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
        // 이 컨버터는 "obe/" 또는 "bit/"로 시작하는 토픽을 처리합니다.
        return topic != null && (topic.startsWith("obe/") || topic.startsWith("bit/"));
    }
}