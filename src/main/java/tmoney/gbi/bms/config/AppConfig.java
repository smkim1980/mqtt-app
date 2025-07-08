package tmoney.gbi.bms.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final List<ProcessInterface> processInterfaces;

    /**
     * 애플리케이션 시작 시 모든 DB 워커를 초기화하고 실행합니다.
     */
    @Bean
    public ApplicationRunner dbWorkers() {
        return args -> {
            processInterfaces.forEach(ProcessInterface::startBatchProcessors);
        };
    }
}
