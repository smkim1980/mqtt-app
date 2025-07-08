package tmoney.gbi.bms.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tmoney.gbi.bms.config.queue.QueueFactory;
import tmoney.gbi.bms.handler.MessageHandler;

@Component
@RequiredArgsConstructor
public class QueueingMessageHandler implements MessageHandler<byte[]> {

    private final QueueFactory queueFactory;

    @Override
    public boolean canHandle(String topic) {
        // 이 핸들러는 모든 토픽의 메시지를 큐에 넣는 역할을 하므로 항상 true를 반환합니다.
        // 특정 토픽 패턴만 큐에 넣고 싶다면 여기에 로직을 추가할 수 있습니다.
        // 예: return topic.startsWith("obe/") || topic.startsWith("bit/");
        return true;
    }

    @Override
    public void handle(String topic, byte[] message) {
        queueFactory.getQueue(topic).offer(message);
    }
}
