package tmoney.gbi.bms.common.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class QueueModel {

    private String topic;
    private byte[] payload;
}
