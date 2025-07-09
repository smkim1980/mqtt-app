package tmoney.gbi.bms.common.constant;

import lombok.Getter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum MqttTopic {
    // OBE Topics
    OBE_TBUS_INB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.TBUS, TopicRuleNames.InbOutB.INB),
    OBE_TBUS_OUTB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.TBUS, TopicRuleNames.InbOutB.OUTB),
    OBE_BBUB_INB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.BBUB, TopicRuleNames.InbOutB.INB),
    OBE_BBUB_OUTB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.BBUB, TopicRuleNames.InbOutB.OUTB),
    OBE_MBUB_INB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.MBUB, TopicRuleNames.InbOutB.INB),
    OBE_MBUB_OUTB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.MBUB, TopicRuleNames.InbOutB.OUTB),
    OBE_OBUB_INB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.OBUB, TopicRuleNames.InbOutB.INB),
    OBE_OBUB_OUTB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.OBUB, TopicRuleNames.InbOutB.OUTB),
    OBE_TOBUS_INB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.TOBUS, TopicRuleNames.InbOutB.INB),
    OBE_TOBUS_OUTB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.TOBUS, TopicRuleNames.InbOutB.OUTB),
    OBE_ABUB_INB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.ABUB, TopicRuleNames.InbOutB.INB),
    OBE_ABUB_OUTB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.ABUB, TopicRuleNames.InbOutB.OUTB),
    OBE_APBUB_INB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.APBUB, TopicRuleNames.InbOutB.INB),
    OBE_APBUB_OUTB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.APBUB, TopicRuleNames.InbOutB.OUTB),
    OBE_NBUB_INB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.NBUB, TopicRuleNames.InbOutB.INB),
    OBE_NBUB_OUTB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.NBUB, TopicRuleNames.InbOutB.OUTB),
    OBE_GYEONGGI_INB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.GYEONGGI, TopicRuleNames.InbOutB.INB),
    OBE_GYEONGGI_OUTB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.GYEONGGI, TopicRuleNames.InbOutB.OUTB),
    OBE_INOHEON_INB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.INOHEON, TopicRuleNames.InbOutB.INB),
    OBE_INOHEON_OUTB(TopicRuleNames.InfoType.OBE, TopicRuleNames.RoutePoint.INOHEON, TopicRuleNames.InbOutB.OUTB),

    // BIT Topics
    BIT_TBUS_INB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.TBUS, TopicRuleNames.InbOutB.INB),
    BIT_TBUS_OUTB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.TBUS, TopicRuleNames.InbOutB.OUTB),
    BIT_BBUB_INB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.BBUB, TopicRuleNames.InbOutB.INB),
    BIT_BBUB_OUTB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.BBUB, TopicRuleNames.InbOutB.OUTB),
    BIT_MBUB_INB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.MBUB, TopicRuleNames.InbOutB.INB),
    BIT_MBUB_OUTB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.MBUB, TopicRuleNames.InbOutB.OUTB),
    BIT_OBUB_INB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.OBUB, TopicRuleNames.InbOutB.INB),
    BIT_OBUB_OUTB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.OBUB, TopicRuleNames.InbOutB.OUTB),
    BIT_TOBUS_INB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.TOBUS, TopicRuleNames.InbOutB.INB),
    BIT_TOBUS_OUTB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.TOBUS, TopicRuleNames.InbOutB.OUTB),
    BIT_ABUB_INB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.ABUB, TopicRuleNames.InbOutB.INB),
    BIT_ABUB_OUTB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.ABUB, TopicRuleNames.InbOutB.OUTB),
    BIT_APBUB_INB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.APBUB, TopicRuleNames.InbOutB.INB),
    BIT_APBUB_OUTB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.APBUB, TopicRuleNames.InbOutB.OUTB),
    BIT_NBUB_INB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.NBUB, TopicRuleNames.InbOutB.INB),
    BIT_NBUB_OUTB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.NBUB, TopicRuleNames.InbOutB.OUTB),
    BIT_GYEONGGI_INB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.GYEONGGI, TopicRuleNames.InbOutB.INB),
    BIT_GYEONGGI_OUTB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.GYEONGGI, TopicRuleNames.InbOutB.OUTB),
    BIT_INOHEON_INB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.INOHEON, TopicRuleNames.InbOutB.INB),
    BIT_INOHEON_OUTB(TopicRuleNames.InfoType.BIT, TopicRuleNames.RoutePoint.INOHEON, TopicRuleNames.InbOutB.OUTB),

    // SUB Topics
    SUB_TBUS_INB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.TBUS, TopicRuleNames.InbOutB.INB),
    SUB_TBUS_OUTB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.TBUS, TopicRuleNames.InbOutB.OUTB),
    SUB_BBUB_INB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.BBUB, TopicRuleNames.InbOutB.INB),
    SUB_BBUB_OUTB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.BBUB, TopicRuleNames.InbOutB.OUTB),
    SUB_MBUB_INB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.MBUB, TopicRuleNames.InbOutB.INB),
    SUB_MBUB_OUTB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.MBUB, TopicRuleNames.InbOutB.OUTB),
    SUB_OBUB_INB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.OBUB, TopicRuleNames.InbOutB.INB),
    SUB_OBUB_OUTB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.OBUB, TopicRuleNames.InbOutB.OUTB),
    SUB_TOBUS_INB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.TOBUS, TopicRuleNames.InbOutB.INB),
    SUB_TOBUS_OUTB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.TOBUS, TopicRuleNames.InbOutB.OUTB),
    SUB_ABUB_INB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.ABUB, TopicRuleNames.InbOutB.INB),
    SUB_ABUB_OUTB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.ABUB, TopicRuleNames.InbOutB.OUTB),
    SUB_APBUB_INB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.APBUB, TopicRuleNames.InbOutB.INB),
    SUB_APBUB_OUTB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.APBUB, TopicRuleNames.InbOutB.OUTB),
    SUB_NBUB_INB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.NBUB, TopicRuleNames.InbOutB.INB),
    SUB_NBUB_OUTB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.NBUB, TopicRuleNames.InbOutB.OUTB),
    SUB_GYEONGGI_INB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.GYEONGGI, TopicRuleNames.InbOutB.INB),
    SUB_GYEONGGI_OUTB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.GYEONGGI, TopicRuleNames.InbOutB.OUTB),
    SUB_INOHEON_INB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.INOHEON, TopicRuleNames.InbOutB.INB),
    SUB_INOHEON_OUTB(TopicRuleNames.InfoType.SUB, TopicRuleNames.RoutePoint.INOHEON, TopicRuleNames.InbOutB.OUTB),

    // DBB Topics
    DBB_TBUS_INB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.TBUS, TopicRuleNames.InbOutB.INB),
    DBB_TBUS_OUTB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.TBUS, TopicRuleNames.InbOutB.OUTB),
    DBB_BBUB_INB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.BBUB, TopicRuleNames.InbOutB.INB),
    DBB_BBUB_OUTB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.BBUB, TopicRuleNames.InbOutB.OUTB),
    DBB_MBUB_INB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.MBUB, TopicRuleNames.InbOutB.INB),
    DBB_MBUB_OUTB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.MBUB, TopicRuleNames.InbOutB.OUTB),
    DBB_OBUB_INB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.OBUB, TopicRuleNames.InbOutB.INB),
    DBB_OBUB_OUTB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.OBUB, TopicRuleNames.InbOutB.OUTB),
    DBB_TOBUS_INB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.TOBUS, TopicRuleNames.InbOutB.INB),
    DBB_TOBUS_OUTB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.TOBUS, TopicRuleNames.InbOutB.OUTB),
    DBB_ABUB_INB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.ABUB, TopicRuleNames.InbOutB.INB),
    DBB_ABUB_OUTB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.ABUB, TopicRuleNames.InbOutB.OUTB),
    DBB_APBUB_INB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.APBUB, TopicRuleNames.InbOutB.INB),
    DBB_APBUB_OUTB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.APBUB, TopicRuleNames.InbOutB.OUTB),
    DBB_NBUB_INB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.NBUB, TopicRuleNames.InbOutB.INB),
    DBB_NBUB_OUTB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.NBUB, TopicRuleNames.InbOutB.OUTB),
    DBB_GYEONGGI_INB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.GYEONGGI, TopicRuleNames.InbOutB.INB),
    DBB_GYEONGGI_OUTB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.GYEONGGI, TopicRuleNames.InbOutB.OUTB),
    DBB_INOHEON_INB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.INOHEON, TopicRuleNames.InbOutB.INB),
    DBB_INOHEON_OUTB(TopicRuleNames.InfoType.DBB, TopicRuleNames.RoutePoint.INOHEON, TopicRuleNames.InbOutB.OUTB),

    // API Topics
    API_TBUS_INB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.TBUS, TopicRuleNames.InbOutB.INB),
    API_TBUS_OUTB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.TBUS, TopicRuleNames.InbOutB.OUTB),
    API_BBUB_INB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.BBUB, TopicRuleNames.InbOutB.INB),
    API_BBUB_OUTB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.BBUB, TopicRuleNames.InbOutB.OUTB),
    API_MBUB_INB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.MBUB, TopicRuleNames.InbOutB.INB),
    API_MBUB_OUTB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.MBUB, TopicRuleNames.InbOutB.OUTB),
    API_OBUB_INB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.OBUB, TopicRuleNames.InbOutB.INB),
    API_OBUB_OUTB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.OBUB, TopicRuleNames.InbOutB.OUTB),
    API_TOBUS_INB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.TOBUS, TopicRuleNames.InbOutB.INB),
    API_TOBUS_OUTB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.TOBUS, TopicRuleNames.InbOutB.OUTB),
    API_ABUB_INB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.ABUB, TopicRuleNames.InbOutB.INB),
    API_ABUB_OUTB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.ABUB, TopicRuleNames.InbOutB.OUTB),
    API_APBUB_INB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.APBUB, TopicRuleNames.InbOutB.INB),
    API_APBUB_OUTB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.APBUB, TopicRuleNames.InbOutB.OUTB),
    API_NBUB_INB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.NBUB, TopicRuleNames.InbOutB.INB),
    API_NBUB_OUTB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.NBUB, TopicRuleNames.InbOutB.OUTB),
    API_GYEONGGI_INB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.GYEONGGI, TopicRuleNames.InbOutB.INB),
    API_GYEONGGI_OUTB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.GYEONGGI, TopicRuleNames.InbOutB.OUTB),
    API_INOHEON_INB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.INOHEON, TopicRuleNames.InbOutB.INB),
    API_INOHEON_OUTB(TopicRuleNames.InfoType.API, TopicRuleNames.RoutePoint.INOHEON, TopicRuleNames.InbOutB.OUTB);

    private final String infoType;
    private final String routePoint;
    private final String inbOutb;

    private static final String SLASH = "/";
    private static final String ALL_SUB_LEVELS = "/#";

    MqttTopic(String infoType, String routePoint, String inbOutb) {
        this.infoType = infoType;
        this.routePoint = routePoint;
        this.inbOutb = inbOutb;
    }

    public String getTopicString() {
        return infoType + SLASH + routePoint + SLASH + inbOutb + ALL_SUB_LEVELS;
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
