package tmoney.gbi.bms.converter;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Protobuf 메시지(byte[])를 특정 DTO 타입으로 변환하는 인터페이스
 *
 * @param <T> 변환할 DTO 타입
 */
public interface MessageConverter<T> {

    /**
     * Protobuf 바이트 배열을 DTO 객체로 변환합니다.
     *
     * @param message a byte array containing the protobuf message
     * @return the converted DTO object
     * @throws InvalidProtocolBufferException if the byte array is not a valid protobuf message
     */
    T convert(byte[] message) throws InvalidProtocolBufferException;

    /**
     * 이 컨버터가 처리할 수 있는 토픽인지 확인합니다.
     *
     * @param topic the MQTT topic
     * @return true if this converter can handle the topic, false otherwise
     */
    boolean canHandle(String topic);


    T encrypt(T dto);
}
