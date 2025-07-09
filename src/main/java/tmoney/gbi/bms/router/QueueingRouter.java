package tmoney.gbi.bms.router;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tmoney.gbi.bms.common.constant.MqttTopic;
import tmoney.gbi.bms.common.queue.QueueFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueueingRouter {
    private final QueueFactory queueFactory;

    public void route(String topic, byte[] message) {
        if (canHandle(topic)) {
            handle(topic, message);
        } else {
            throw new IllegalArgumentException("No handler found for topic: " + topic);
        }
    }

    private boolean canHandle(String topic) {
        // MqttTopic enum에 정의된 토픽만 처리하도록 수정
        return Arrays.stream(MqttTopic.getAllTopicsAsArray()).anyMatch(topic::startsWith);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void handle(String topic, byte[] message) {
        try {
            queueFactory.getQueue(topic).offer(message , 5, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for queue to fire ::topic:<{}> , MESSAGE <{}>", topic ,new String(message , StandardCharsets.UTF_8));
        }
    }
}
