package tmoney.gbi.bms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BmsMqttProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BmsMqttProcessorApplication.class, args);
    }

}
