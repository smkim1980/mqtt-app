package tmoney.gbi.bms;

import lombok.extern.slf4j.Slf4j;
import tmoney.gbi.bms.common.constant.MqttTopic;

import java.util.Arrays;

@Slf4j
public class SampleTest {

    public static void main(String[] args) {
        String topic="tbe/tbus/inb/187000001/187000010/6200/190000111/10";
        if(Arrays.stream(MqttTopic.getAllTopicsAsArray()).anyMatch(topic::startsWith)){
            log.info("topic :: {} is true" , topic);
        }
    }
}
