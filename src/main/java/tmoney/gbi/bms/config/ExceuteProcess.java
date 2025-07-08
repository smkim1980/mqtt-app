package tmoney.gbi.bms.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tmoney.gbi.bms.common.constant.MqttTopicConstants;
import tmoney.gbi.bms.config.processor.BatchProcessorRunnable;
import tmoney.gbi.bms.config.processor.DataProcessor;
import tmoney.gbi.bms.config.processor.ProcessorProvider;
import tmoney.gbi.bms.config.properties.AppProperties;
import tmoney.gbi.bms.config.queue.QueueFactory;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executor;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceuteProcess implements ProcessInterface {

    private final AppProperties appProperties;
    private final Executor dbWorkerExecutor;
    private final QueueFactory queueFactory;
    private final ProcessorProvider processorProvider;

    @PostConstruct
    public void init() {
        startBatchProcessors();
    }

    @Override
    public void startBatchProcessors() {
        int threadCount = appProperties.getDbWorker().getThreads();
        int batchSize = appProperties.getDbWorker().getBatchSize();
        List<String> topicPrefixes = MqttTopicConstants.TOPIC_PREFIXES;

        // 각 토픽 프리픽스에 대해 배치 프로세서 생성 및 실행
        for (String topicPrefix : topicPrefixes) {
            DataProcessor<?> dataProcessor = processorProvider.getProcessor(topicPrefix);

            if (dataProcessor == null) {
                log.warn("No processor found for topic prefix: '{}'. Skipping.", topicPrefix);
                continue;
            }

            log.info("Initializing batch processors for topic group: '{}' with processor: {}",
                    topicPrefix, dataProcessor.getClass().getSimpleName());

            for (int i = 0; i < threadCount; i++) {
                // DataProcessor가 제네릭을 사용하므로, 이를 사용하는 Runnable도 제네릭 타입이어야 합니다.
                // ? 와일드카드를 사용하여 이를 해결합니다.
                BatchProcessorRunnable<?> processorRunnable = new BatchProcessorRunnable<>(
                        topicPrefix + "-Processor-" + i,
                        queueFactory.getQueue(topicPrefix),
                        dataProcessor,
                        batchSize
                );
                dbWorkerExecutor.execute(processorRunnable);
            }
        }
    }
}
