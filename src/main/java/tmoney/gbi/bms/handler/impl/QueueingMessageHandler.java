package tmoney.gbi.bms.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tmoney.gbi.bms.config.queue.QueueFactory;
import tmoney.gbi.bms.handler.MessageHandler;

import tmoney.gbi.bms.common.constant.MqttTopic;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class QueueingMessageHandler implements MessageHandler<byte[]> {

    private final QueueFactory queueFactory;

    @Override
    public boolean canHandle(String topic) {
        // MqttTopic enum에 정의된 토픽만 처리하도록 수정
        return Arrays.stream(MqttTopic.getAllTopicsAsArray()).anyMatch(topic::startsWith);
    }

    @Override
    public void handle(String topic, byte[] message) {
        queueFactory.getQueue(topic).offer(message);
    }
}
