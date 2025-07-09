package tmoney.gbi.bms;

import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import tmoney.gbi.bms.common.queue.QueueFactory;
import tmoney.gbi.bms.proto.Location;
import tmoney.gbi.bms.router.QueueingRouter;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(properties = {
        "spring.main.banner-mode=off",
        "logging.level.root=INFO",
        "app.db-worker.threads=5" // 테스트를 위해 스레드 수 조정
})
public class MessageProcessingIntegrationTest {

    @Autowired
    private QueueingRouter router;

    @Autowired
    private QueueFactory queueFactory;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String TEST_TOPIC_PREFIX = "obe/tbus/inb";
    private static final String TEST_TOPIC = TEST_TOPIC_PREFIX + "/test-vehicle";

    @BeforeEach
    void setUp() {
        // 각 테스트 전에 큐와 DB 테이블을 비웁니다.
        queueFactory.getQueue(TEST_TOPIC).clear();
        jdbcTemplate.execute("TRUNCATE TABLE ENCRYPTED_LOCATION");
    }

    @Test
    @DisplayName("대량(1000건) 메시지 처리 및 DB 저장 성능 테스트")
    void testHighVolumeMessageProcessingAndDbInsertion() {
        // given
        final int messageCount = 1000;
        // 이번 테스트 실행의 고유 시간 범위
        final Instant baseTime = Instant.now();
        final long startTimeMillis = baseTime.toEpochMilli();
        final long endTimeMillis = startTimeMillis + messageCount;

        List<Location> locations = new ArrayList<>();

        IntStream.range(0, messageCount).forEach(i -> {
            Instant occurAt = baseTime.plusMillis(i);
            Location location = Location.newBuilder()
                    .setOccurAt(Timestamp.newBuilder()
                            .setSeconds(occurAt.getEpochSecond())
                            .setNanos(occurAt.getNano()).build())
                    .setLatitude(37.5665 + (i * 0.00001))
                    .setLongitude(126.9780 + (i * 0.00001))
                    .build();
            locations.add(location);
        });

        // when
        log.info("===== 테스트 시작: {}건의 메시지 발행 =====", messageCount);
        Instant testStartTime = Instant.now();

        locations.forEach(loc -> {
            router.route(TEST_TOPIC, loc.toByteArray());
        });

        // then
        BlockingQueue<byte[]> queue = queueFactory.getQueue(TEST_TOPIC);

        // 1. 모든 메시지가 큐에서 소비되고, DB에 모두 저장될 때까지 대기
        String countSql = "SELECT COUNT(*) FROM ENCRYPTED_LOCATION WHERE occur_dt >= ? AND occur_dt < ?";
        Awaitility.await()
                .atMost(60, TimeUnit.SECONDS) // 최대 60초 대기
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .until(() -> {
                    int dbRowCount = jdbcTemplate.queryForObject(countSql, Integer.class, startTimeMillis, endTimeMillis);
                    log.info("Current DB row count: {}", dbRowCount);
                    // 큐가 비어있고, DB에 모든 데이터가 들어갔는지 확인
                    return queue.isEmpty() && dbRowCount == messageCount;
                });

        Instant testEndTime = Instant.now();
        log.info("===== 테스트 종료: 모든 메시지 처리 완료 =====");

        // 2. 최종 결과 검증
        long totalTimeSeconds = Duration.between(testStartTime, testEndTime).getSeconds();
        Integer finalDbCount = jdbcTemplate.queryForObject(countSql, Integer.class, startTimeMillis, endTimeMillis);

        // 3. 중복 데이터 검증 (동일한 EVT_DTM이 2개 이상인 경우)
        String duplicateCheckSql = "SELECT COUNT(*) FROM (SELECT COUNT(*) as cnt FROM ENCRYPTED_LOCATION WHERE occur_dt >= ? AND occur_dt < ? GROUP BY occur_dt HAVING COUNT(*) > 1) AS duplicates";
        Integer duplicateCount = jdbcTemplate.queryForObject(duplicateCheckSql, Integer.class, startTimeMillis, endTimeMillis);

        // 4. 결과 출력
        log.info("\n========== 테스트 결과 ==========\n" +
                        "총 처리 시간: {}초\n" +
                        "DB에 저장된 총 건수: {}\n" +
                        "중복 저장된 데이터 건수: {}\n" +
                        "===============================",
                totalTimeSeconds, finalDbCount, duplicateCount);

        assertThat(finalDbCount).isEqualTo(messageCount);
        assertThat(duplicateCount).isEqualTo(0);
    }
}