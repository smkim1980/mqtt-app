package tmoney.gbi.bms.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tmoney.gbi.bms.common.constant.MqttTopic;
import tmoney.gbi.bms.common.properties.AppProperties;
import tmoney.gbi.bms.common.queue.QueueFactory;
import tmoney.gbi.bms.config.runnable.BatchProcessorRunnable;
import tmoney.gbi.bms.config.runnable.ProcessorProvider;
import tmoney.gbi.bms.processor.DataProcessor;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceuteProcess {

    private final AppProperties appProperties;
    private final Executor dbWorkerExecutor;
    private final QueueFactory queueFactory;
    private final ProcessorProvider processorProvider;

    @PostConstruct
    public void startBatchProcessors() {
        final int threadCount = appProperties.getDbWorker().getThreads();
        final int batchSize = appProperties.getDbWorker().getBatchSize();

        Arrays.stream(MqttTopic.getAllTopicsAsArray())
                .map(topic -> {
                    DataProcessor<?> processor = processorProvider.getProcessor(topic);
                    if (processor == null) {
                        log.warn("No processor found for topic: '{}'. Skipping.", topic);
                        return null; // 처리할 프로세서가 없는 경우 null 반환
                    }
                    return new Object[]{topic, processor}; // 토픽과 프로세서를 함께 전달
                })
                .filter(Objects::nonNull) // 프로세서가 없는 토픽은 제외
                .forEach(topicAndProcessor -> {
                    String topic = (String) topicAndProcessor[0];
                    DataProcessor<?> processor = (DataProcessor<?>) topicAndProcessor[1];

                    log.info("Initializing {} batch processors for topic group: '{}' with processor: {}",
                            threadCount, topic, processor.getClass().getSimpleName());

                    // 지정된 스레드 수만큼 처리기(Runnable)를 생성하여 가상 스레드로 실행
                    IntStream.range(0, threadCount)
                            .mapToObj(i -> new BatchProcessorRunnable<>(
                                    topic + "-Processor-" + i,
                                    queueFactory.getQueue(topic),
                                    processor,
                                    batchSize
                            ))
                            .forEach(dbWorkerExecutor::execute);
                });
    }
}
