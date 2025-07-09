package tmoney.gbi.bms.common.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static tmoney.gbi.bms.common.constant.TopicRuleNames.InbOutB.INB;
import static tmoney.gbi.bms.common.constant.TopicRuleNames.InbOutB.OUTB;
import static tmoney.gbi.bms.common.constant.TopicRuleNames.InfoType.*;
import static tmoney.gbi.bms.common.constant.TopicRuleNames.RoutePoint.*;

@Getter
public enum MqttTopic {
    // OBE Topics
    OBE_TBUS_INB(OBE, TBUS, INB),
    OBE_TBUS_OUTB(OBE, TBUS, OUTB),
    OBE_BBUB_INB(OBE, BBUB, INB),
    OBE_BBUB_OUTB(OBE, BBUB, OUTB),
    OBE_MBUB_INB(OBE, MBUB, INB),
    OBE_MBUB_OUTB(OBE, MBUB, OUTB),
    OBE_OBUB_INB(OBE, OBUB, INB),
    OBE_OBUB_OUTB(OBE, OBUB, OUTB),
    OBE_TOBUS_INB(OBE, TOBUS, INB),
    OBE_TOBUS_OUTB(OBE, TOBUS, OUTB),
    OBE_ABUB_INB(OBE, ABUB, INB),
    OBE_ABUB_OUTB(OBE, ABUB, OUTB),
    OBE_APBUB_INB(OBE, APBUB, INB),
    OBE_APBUB_OUTB(OBE, APBUB, OUTB),
    OBE_NBUB_INB(OBE, NBUB, INB),
    OBE_NBUB_OUTB(OBE, NBUB, OUTB),
    OBE_GYEONGGI_INB(OBE, GYEONGGI, INB),
    OBE_GYEONGGI_OUTB(OBE, GYEONGGI, OUTB),
    OBE_INOHEON_INB(OBE, INOHEON, INB),
    OBE_INOHEON_OUTB(OBE, INOHEON, OUTB),

    // BIT Topics
    BIT_TBUS_INB(BIT, TBUS, INB),
    BIT_TBUS_OUTB(BIT, TBUS, OUTB),
    BIT_BBUB_INB(BIT, BBUB, INB),
    BIT_BBUB_OUTB(BIT, BBUB, OUTB),
    BIT_MBUB_INB(BIT, MBUB, INB),
    BIT_MBUB_OUTB(BIT, MBUB, OUTB),
    BIT_OBUB_INB(BIT, OBUB, INB),
    BIT_OBUB_OUTB(BIT, OBUB, OUTB),
    BIT_TOBUS_INB(BIT, TOBUS, INB),
    BIT_TOBUS_OUTB(BIT, TOBUS, OUTB),
    BIT_ABUB_INB(BIT, ABUB, INB),
    BIT_ABUB_OUTB(BIT, ABUB, OUTB),
    BIT_APBUB_INB(BIT, APBUB, INB),
    BIT_APBUB_OUTB(BIT, APBUB, OUTB),
    BIT_NBUB_INB(BIT, NBUB, INB),
    BIT_NBUB_OUTB(BIT, NBUB, OUTB),
    BIT_GYEONGGI_INB(BIT, GYEONGGI, INB),
    BIT_GYEONGGI_OUTB(BIT, GYEONGGI, OUTB),
    BIT_INOHEON_INB(BIT, INOHEON, INB),
    BIT_INOHEON_OUTB(BIT, INOHEON, OUTB),

    // SUB Topics
    SUB_TBUS_INB(SUB, TBUS, INB),
    SUB_TBUS_OUTB(SUB, TBUS, OUTB),
    SUB_BBUB_INB(SUB, BBUB, INB),
    SUB_BBUB_OUTB(SUB, BBUB, OUTB),
    SUB_MBUB_INB(SUB, MBUB, INB),
    SUB_MBUB_OUTB(SUB, MBUB, OUTB),
    SUB_OBUB_INB(SUB, OBUB, INB),
    SUB_OBUB_OUTB(SUB, OBUB, OUTB),
    SUB_TOBUS_INB(SUB, TOBUS, INB),
    SUB_TOBUS_OUTB(SUB, TOBUS, OUTB),
    SUB_ABUB_INB(SUB, ABUB, INB),
    SUB_ABUB_OUTB(SUB, ABUB, OUTB),
    SUB_APBUB_INB(SUB, APBUB, INB),
    SUB_APBUB_OUTB(SUB, APBUB, OUTB),
    SUB_NBUB_INB(SUB, NBUB, INB),
    SUB_NBUB_OUTB(SUB, NBUB, OUTB),
    SUB_GYEONGGI_INB(SUB, GYEONGGI, INB),
    SUB_GYEONGGI_OUTB(SUB, GYEONGGI, OUTB),
    SUB_INOHEON_INB(SUB, INOHEON, INB),
    SUB_INOHEON_OUTB(SUB, INOHEON, OUTB),

    // DBB Topics
    DBB_TBUS_INB(DBB, TBUS, INB),
    DBB_TBUS_OUTB(DBB, TBUS, OUTB),
    DBB_BBUB_INB(DBB, BBUB, INB),
    DBB_BBUB_OUTB(DBB, BBUB, OUTB),
    DBB_MBUB_INB(DBB, MBUB, INB),
    DBB_MBUB_OUTB(DBB, MBUB, OUTB),
    DBB_OBUB_INB(DBB, OBUB, INB),
    DBB_OBUB_OUTB(DBB, OBUB, OUTB),
    DBB_TOBUS_INB(DBB, TOBUS, INB),
    DBB_TOBUS_OUTB(DBB, TOBUS, OUTB),
    DBB_ABUB_INB(DBB, ABUB, INB),
    DBB_ABUB_OUTB(DBB, ABUB, OUTB),
    DBB_APBUB_INB(DBB, APBUB, INB),
    DBB_APBUB_OUTB(DBB, APBUB, OUTB),
    DBB_NBUB_INB(DBB, NBUB, INB),
    DBB_NBUB_OUTB(DBB, NBUB, OUTB),
    DBB_GYEONGGI_INB(DBB, GYEONGGI, INB),
    DBB_GYEONGGI_OUTB(DBB, GYEONGGI, OUTB),
    DBB_INOHEON_INB(DBB, INOHEON, INB),
    DBB_INOHEON_OUTB(DBB, INOHEON, OUTB),

    // API Topics
    API_TBUS_INB(API, TBUS, INB),
    API_TBUS_OUTB(API, TBUS, OUTB),
    API_BBUB_INB(API, BBUB, INB),
    API_BBUB_OUTB(API, BBUB, OUTB),
    API_MBUB_INB(API, MBUB, INB),
    API_MBUB_OUTB(API, MBUB, OUTB),
    API_OBUB_INB(API, OBUB, INB),
    API_OBUB_OUTB(API, OBUB, OUTB),
    API_TOBUS_INB(API, TOBUS, INB),
    API_TOBUS_OUTB(API, TOBUS, OUTB),
    API_ABUB_INB(API, ABUB, INB),
    API_ABUB_OUTB(API, ABUB, OUTB),
    API_APBUB_INB(API, APBUB, INB),
    API_APBUB_OUTB(API, APBUB, OUTB),
    API_NBUB_INB(API, NBUB, INB),
    API_NBUB_OUTB(API, NBUB, OUTB),
    API_GYEONGGI_INB(API, GYEONGGI, INB),
    API_GYEONGGI_OUTB(API, GYEONGGI, OUTB),
    API_INOHEON_INB(API, INOHEON, INB),
    API_INOHEON_OUTB(API, INOHEON, OUTB);

    private final String infoType;
    private final String routePoint;
    private final String inbOutb;

    private static final String SLASH = "/";

    MqttTopic(String infoType, String routePoint, String inbOutb) {
        this.infoType = infoType;
        this.routePoint = routePoint;
        this.inbOutb = inbOutb;
    }

    public String getTopicString() {
        return infoType + SLASH + routePoint + SLASH + inbOutb;
    }

    public static List<String> getAllTopics() {
        return Arrays.stream(MqttTopic.values())
                .map(MqttTopic::getTopicString)
                .collect(Collectors.toList());
    }

    public static String[] getAllTopicsAsArray() {
        return Arrays.stream(MqttTopic.values())
                .map(MqttTopic::getTopicString)
                .toArray(String[]::new);
    }
}
