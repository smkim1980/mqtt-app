package tmoney.gbi.bms.config.runnable;

import lombok.extern.slf4j.Slf4j;
import tmoney.gbi.bms.common.queue.QueueModel;
import tmoney.gbi.bms.processor.DataProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BatchProcessorRunnable<T> implements Runnable {

    private final String processorName;
    private final BlockingQueue<QueueModel> queue;
    private final DataProcessor<T> dataProcessor;
    private final int batchSize;

    public BatchProcessorRunnable(String processorName, BlockingQueue<QueueModel> queue, DataProcessor<T> dataProcessor, int batchSize) {
        this.processorName = processorName;
        this.queue = queue;
        this.dataProcessor = dataProcessor;
        this.batchSize = batchSize;
    }

    @Override
    public void run() {
        log.info("Processor [{}] started.", processorName);
        List<T> batchList = new ArrayList<>(batchSize);

        while (!Thread.currentThread().isInterrupted()) {
            try {
                QueueModel message = queue.poll(1 , TimeUnit.SECONDS); // Objects.requireNonNull(queue.poll(1, TimeUnit.SECONDS)).getPayload();
                if (message == null) {
                    // Process batch if timeout occurs and list is not empty
                    if (!batchList.isEmpty()) {
                        processBatch(batchList);
                    }
                    continue;
                }

                T dto = dataProcessor.convert(message);
                dataProcessor.process(dto);
                batchList.add(dto);

                if (batchList.size() >= batchSize) {
                    processBatch(batchList);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Processor [{}] interrupted.", processorName);
                break;
            } catch (Exception e) {
                log.error("[{}] Error processing message. Clearing batch.", processorName, e);
                batchList.clear();
            }
        }

        // Process any remaining items before shutting down
        if (!batchList.isEmpty()) {
            log.info("Processor [{}] processing final batch of {} items before shutdown.", processorName, batchList.size());
            processBatch(batchList);
        }
        log.info("Processor [{}] stopped.", processorName);
    }

    private void processBatch(List<T> batchList) {
        if (batchList.isEmpty()) {
            return;
        }
        try {
            // Drain remaining messages from the queue to fill up the batch
            List<QueueModel> remainingMessages = new ArrayList<>();
            queue.drainTo(remainingMessages, batchSize - batchList.size());

            for (QueueModel remainingMessage : remainingMessages) {
                try {
                    T remainingDto = dataProcessor.convert(remainingMessage);
                    dataProcessor.process(remainingDto);
                    batchList.add(remainingDto);
                } catch (Exception e) {
                    log.error("[{}] Failed to convert message during batch drain. Skipping message.", processorName, e);
                }
            }
            
            dataProcessor.batchInsert(batchList);
        } catch (Exception e) {
            log.error("[{}] Error writing batch to DB.", processorName, e);
        } finally {
            batchList.clear();
        }
    }
}
