package tmoney.gbi.bms.config.runnable;

import org.springframework.stereotype.Component;
import tmoney.gbi.bms.processor.DataProcessor;

import java.util.List;

/**
 * 토픽에 맞는 DataProcessor를 제공하는 팩토리 클래스.
 */
@Component
public class ProcessorProvider {

    private final List<DataProcessor<?>> processors;

    public ProcessorProvider(List<DataProcessor<?>> processors) {
        this.processors = processors;
    }

    /**
     * 주어진 토픽을 지원하는 첫 번째 DataProcessor를 반환합니다.
     *
     * @param topic 토픽
     * @return 지원하는 DataProcessor, 없으면 null
     */
    public DataProcessor<?> getProcessor(String topic) {
        return processors.stream()
                .filter(p -> p.supports(topic))
                .findFirst()
                .orElse(null);
    }
}
