package tmoney.gbi.bms.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tmoney.gbi.bms.common.constant.MqttTopic;
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
        String[] topics = MqttTopic.getAllTopicsAsArray();

        for (String topic : topics) {
            DataProcessor<?> dataProcessor = processorProvider.getProcessor(topic);

            if (dataProcessor == null) {
                log.warn("No processor found for topic: '{}'. Skipping.", topic);
                continue;
            }

            log.info("Initializing batch processors for topic group: '{}' with processor: {}",
                    topic, dataProcessor.getClass().getSimpleName());

            for (int i = 0; i < threadCount; i++) {
                // DataProcessor가 제네릭을 사용하므로, 이를 사용하는 Runnable도 제네릭 타입이어야 합니다.
                // ? 와일드카드를 사용하여 이를 해결합니다.
                BatchProcessorRunnable<?> processorRunnable = new BatchProcessorRunnable<>(
                        topic + "-Processor-" + i,
                        queueFactory.getQueue(topic),
                        dataProcessor,
                        batchSize
                );
                dbWorkerExecutor.execute(processorRunnable);
            }
        }
    }
}
