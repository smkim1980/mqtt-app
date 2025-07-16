package tmoney.gbi.bms.config;



import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;

/**
 * packageName     : tmoney.gbi.bms.config
 * fileName       : H2ConsoleConfig.java
 * author         : rlatn
 * date           : 2025. 07. 16.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025. 07. 16.        rlatn       최초 생성
 */

@Configuration
@Profile("local") // local 프로파일에서만 이 설정을 사용합니다.
public class H2ConsoleConfig {

    @Bean
    public Server h2TcpServer() throws SQLException {
        // H2 TCP 서버를 시작하여 외부 콘솔이나 다른 툴에서 DB에 접근할 수 있게 합니다.
        // 애플리케이션 종료 시 자동으로 서버가 종료됩니다.
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
    }

    @Bean
    public Server h2WebServer() throws SQLException {
        // H2 웹 콘솔을 위한 서버를 시작합니다.
        // 이 서버는 내장된 웹 서버를 사용하므로 spring-boot-starter-web이 필요 없습니다.
        return Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
    }
}
