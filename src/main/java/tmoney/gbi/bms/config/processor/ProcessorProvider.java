package tmoney.gbi.bms.config.processor;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
     * 주어진 토픽 접두사를 지원하는 첫 번째 DataProcessor를 반환합니다.
     *
     * @param topicPrefix 토픽 접두사
     * @return 지원하는 DataProcessor, 없으면 null
     */
    public DataProcessor<?> getProcessor(String topicPrefix) {
        return processors.stream()
                .filter(p -> p.supports(topicPrefix))
                .findFirst()
                .orElse(null);
    }
}
