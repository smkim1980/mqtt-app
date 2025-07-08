package tmoney.gbi.bms.config.queue;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class QueueFactory {

    private final ConcurrentHashMap<String, BlockingQueue<byte[]>> queues = new ConcurrentHashMap<>();

    /**
     * 토픽을 기반으로 적절한 큐를 가져옵니다. 큐가 없으면 새로 생성합니다.
     * 예: "obe/tbus/inb/12345" -> "obe/tbus/inb" 키를 사용하여 큐를 찾습니다.
     *
     * @param topic MQTT topic
     * @return 해당 토픽 그룹을 위한 BlockingQueue
     */
    public BlockingQueue<byte[]> getQueue(String topic) {
        String queueName = extractQueueName(topic);
        return queues.computeIfAbsent(queueName, k -> new LinkedBlockingQueue<>());
    }

    /**
     * 전체 토픽에서 큐 이름으로 사용할 프리픽스를 추출합니다.
     * 예: "obe/tbus/inb/187000001/..." -> "obe/tbus/inb"
     *
     * @param topic The full topic string
     * @return The queue name (topic prefix)
     */
    private String extractQueueName(String topic) {
        if (topic == null || topic.isEmpty()) {
            return "default";
        }
        String[] parts = topic.split("/");
        if (parts.length >= 3) {
            return parts[0] + "/" + parts[1] + "/" + parts[2];
        }
        // 기본적으로 처음 부분만 사용하거나, 규칙에 따라 기본값을 반환합니다.
        return parts[0];
    }
}
