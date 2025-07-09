package tmoney.gbi.bms.processor;

import tmoney.gbi.bms.common.queue.QueueModel;

import java.util.List;

/**
 * 다양한 데이터 유형을 처리하기 위한 전략 인터페이스.
 *
 * @param <T> 처리할 데이터의 DTO 타입
 */
public interface DataProcessor<T> {

    /**
     * 이 프로세서가 주어진 토픽을 지원하는지 여부를 반환합니다.
     *
     * @param topic 토픽
     * @return 지원하면 true, 그렇지 않으면 false
     */
    boolean supports(String topic);

    /**
     * byte[] 메시지를 DTO 객체로 변환합니다.
     *
     * @param message a byte array message
     * @return 변환된 DTO 객체
     * @throws Exception 변환 중 발생한 예외
     */
    T convert(QueueModel message) throws Exception;


    /**
     * DTO 목록을 데이터베이스에 배치 삽입합니다.
     *
     * @param batch 삽입할 DTO 목록
     */
    void batchInsert(List<T> batch);
}
