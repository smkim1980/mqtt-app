package tmoney.gbi.bms.subscription;

import com.github.tocrhz.mqtt.annotation.MqttSubscribe;
import com.github.tocrhz.mqtt.annotation.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tmoney.gbi.bms.router.QueueingRouter;

import static tmoney.gbi.bms.common.constant.TopicRuleNames.InfoType.*;
import static tmoney.gbi.bms.common.constant.TopicRuleNames.QOS_1;

@Component
@Slf4j
@RequiredArgsConstructor
public class MqttMessageSubscription {

    private final QueueingRouter router;

    private static final String ALL_SUB_LEVELS = "/#";

    /**
     * 프로젝트에서 사용하는 모든 주요 토픽을 구독합니다.
     * QoS 레벨은 1로 설정하여 메시지 전달을 최소 한 번 보장합니다.
     */
    @MqttSubscribe(value = {OBE + ALL_SUB_LEVELS, BIT + ALL_SUB_LEVELS, SUB + ALL_SUB_LEVELS, DBB + ALL_SUB_LEVELS, API + ALL_SUB_LEVELS}, qos = QOS_1)
    public void messageSubscription(String topic, @Payload byte[] payload) {
        if (payload == null || payload.length == 0) {
            log.warn("Received empty payload on topic: {}", topic);
            return;
        }
        // 받은 byte[] 메시지를 그대로 라우터에 전달합니다.
        router.route(topic, payload);
    }
}
