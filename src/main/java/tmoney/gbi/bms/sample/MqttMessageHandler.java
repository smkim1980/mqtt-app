package tmoney.gbi.bms.sample;

import com.github.tocrhz.mqtt.annotation.MqttSubscribe;
import com.github.tocrhz.mqtt.annotation.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tmoney.gbi.bms.router.TopicMessageRouter;

@Component
@Slf4j
@RequiredArgsConstructor
public class MqttMessageHandler {

    // 이제 byte[]를 처리하는 라우터를 주입받습니다.
    private final TopicMessageRouter<byte[]> router;

    /**
     * 프로젝트에서 사용하는 모든 주요 토픽을 구독합니다.
     * QoS 레벨은 1로 설정하여 메시지 전달을 최소 한 번 보장합니다.
     */
    @MqttSubscribe(value = {"obe/#", "bit/#", "sub/#", "dbB/#", "api/#"}, qos = 1)
    public void handleMqttMessage(String topic, @Payload byte[] payload) {
        if (payload == null || payload.length == 0) {
            log.warn("Received empty payload on topic: {}", topic);
            return;
        }
        // 받은 byte[] 메시지를 그대로 라우터에 전달합니다.
        router.route(topic, payload);
    }
}
