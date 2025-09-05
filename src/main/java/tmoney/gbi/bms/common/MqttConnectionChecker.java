package tmoney.gbi.bms.common;

import com.github.tocrhz.mqtt.autoconfigure.MqttClientManager;
import com.github.tocrhz.mqtt.publisher.SimpleMqttClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * packageName     : tmoney.gbi.bms.common
 * fileName       : MqttConnectionChecker.java
 * author         : rlatn
 * date           : 2025. 09. 05.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025. 09. 05.        rlatn       최초 생성
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MqttConnectionChecker {

    private final MqttClientManager mqttClientManager;

    @Scheduled(fixedRate = 10000)
    public void checkMqttConnection(){
        //기본 클라이언트 확인
        SimpleMqttClient simpleMqttClient = mqttClientManager.clientGetOrDefault(null);
        if(simpleMqttClient != null && simpleMqttClient.client() != null) {
            log.info("Mqtt Client {} is connected : {}" ,simpleMqttClient.id() ,simpleMqttClient.client().isConnected());
        }else{
            log.warn("Mqtt Client is not available.");
        }

    }
}
