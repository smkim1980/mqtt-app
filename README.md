# MQTT 메시지 처리 애플리케이션 (mqtt-processor-app)

이 프로젝트는 MQTT 메시지를 수신하고, 이를 처리하여 데이터베이스에 저장하는 Spring Boot 기반의 애플리케이션입니다.

## 1. 프로젝트 설계 및 구조

### 1.1. 주요 목적
다양한 MQTT 토픽을 통해 수신되는 메시지(주로 위치 정보)를 효율적으로 파싱, 암호화, 그리고 배치(Batch) 형태로 데이터베이스에 저장하는 것을 목표로 합니다. 높은 처리량과 안정성을 고려하여 설계되었습니다.

### 1.2. 아키텍처 개요
애플리케이션은 다음과 같은 주요 흐름으로 동작합니다:
1.  **메시지 구독:** `MqttMessageSubscription`을 통해 MQTT 브로커의 특정 토픽들을 구독합니다.
2.  **메시지 라우팅 및 큐잉:** 수신된 메시지는 `QueueingRouter`를 통해 토픽별로 분류되어 인메모리 큐에 저장됩니다. (현재 인메모리 큐 사용)
3.  **메시지 처리:** `ExceuteProcess`에 의해 시작된 워커 스레드(가상 스레드)들이 큐에서 메시지를 배치 단위로 가져와 `DataProcessor`를 통해 처리합니다.
4.  **데이터 저장:** 처리된 데이터는 데이터베이스에 배치 삽입됩니다.

### 1.3. 주요 모듈 및 역할

*   `src/main/java/tmoney/gbi/bms/`: 핵심 비즈니스 로직 및 설정 파일
    *   `BmsMqttProcessorAppApplication.java`: Spring Boot 애플리케이션의 진입점.
    *   `common/`: 공통 유틸리티 및 상수.
        *   `constant/`: MQTT 토픽 관련 상수 (`MqttTopic`, `TopicRuleNames`).
        *   `crypto/CryptoService.java`: 데이터 암호화 서비스.
        *   `properties/AppProperties.java`: `application.yml`에서 애플리케이션 관련 속성(DB 워커 스레드 수, 배치 크기, 큐 크기, 큐 크기 등)을 로드합니다.
        *   `queue/QueueFactory.java`: 토픽별 인메모리 큐를 관리하고 제공합니다. (크기 제한 큐 사용)
        *   `util/MqttCommUtil.java`: MQTT 통신 관련 유틸리티.
    *   `config/`: 애플리케이션 전반의 설정 및 초기화 로직.
        *   `AppConfig.java`: 애플리케이션 전반의 설정.
        *   `MqttConfig.java`: MQTT 클라이언트 설정 (주석 처리됨).
        *   `ExceuteProcess.java`: 애플리케이션 시작 시 배치 처리 워커 스레드를 초기화하고 실행합니다.
        *   `async/AsyncConfig.java`: 비동기 작업(DB 워커)을 위한 `Executor` (가상 스레드 기반) 설정.
    *   `converter/`: Protobuf 메시지를 DTO로 변환하는 로직.
        *   `MessageConverter.java`: 메시지 변환 인터페이스.
        *   `LocationMessageConverter.java`: 위치 정보 메시지 변환 구현체.
    *   `mapper/CommonInsertMapper.java`: MyBatis를 이용한 데이터베이스 삽입 인터페이스.
    *   `model/`: 데이터 전송 객체(DTO) 정의 (`EncryptedLocationDto`, `LocationDto`).
    *   `processor/`: 메시지 처리 전략.
        *   `DataProcessor.java`: 데이터 처리 인터페이스.
        *   `impl/LocationDataProcessor.java`: 위치 정보 데이터 처리 구현체.
    *   `router/QueueingRouter.java`: 수신된 메시지를 큐에 라우팅하는 역할.
    *   `subscription/MqttMessageSubscription.java`: MQTT 토픽을 구독하고 메시지를 수신하는 역할.
*   `pom.xml`: Maven 빌드 설정 파일.
*   `src/main/resources/`: 설정 파일 및 SQL 스키마.
    *   `application.yml`: 기본 애플리케이션 설정.
    *   `application-*.yml`: 환경별 설정 (dev, local, prod).
    *   `schema.sql`: 데이터베이스 스키마 정의 (H2 데이터베이스용).

### 1.4. 사용 기술
*   Java 21
*   Spring Boot
*   Apache Maven
*   Eclipse Paho MQTT Client (com.github.tocrhz.mqtt-spring-boot-starter)
*   Google Protobuf
*   Lombok
*   MyBatis
*   H2 Database (개발/테스트용)

## 2. 프로젝트 실행 방법

### 2.1. 사전 준비
*   Java Development Kit (JDK) 21 이상 설치
*   Apache Maven 3.6 이상 설치
*   **MQTT 브로커 설치 및 실행:**
    *   `file/mosquitto-2.0.21a-install-windows-x64.exe` 파일을 사용하여 로컬에 Mosquitto 브로커를 설치할 수 있습니다.
    *   설치 후 Mosquitto 서비스를 시작합니다.

### 2.2. 프로젝트 빌드
프로젝트 루트 디렉토리(`C:\github\mqtt-processor-app`)에서 다음 Maven 명령어를 실행하여 프로젝트를 빌드합니다.

```bash
mvn clean install
```
또는 실행 가능한 JAR 파일을 생성하려면:
```bash
mvn clean package
```

### 2.3. 애플리케이션 실행
빌드가 성공하면 `target/` 디렉토리에 실행 가능한 JAR 파일이 생성됩니다. 다음 명령어를 사용하여 애플리케이션을 실행합니다.

```bash
java -jar target/mqtt-processor-app-*.jar
```
(여기서 `*`는 빌드된 버전에 따라 달라질 수 있습니다. 예: `mqtt-processor-app-0.0.1-SNAPSHOT.jar`)

### 2.4. 설정 변경
`src/main/resources/application.yml` 또는 `application-local.yml` 등의 파일을 수정하여 애플리케이션의 동작을 설정할 수 있습니다. 주요 설정 항목은 다음과 같습니다:
*   `app.db-worker.threads`: DB 워커 스레드 수 (각 토픽 그룹당).
*   `app.db-worker.batch-size`: DB에 한 번에 삽입할 메시지 배치 크기.
*   `app.queue.size`: 인메모리 큐의 최대 크기.

## 3. MQTT 메시지 추가 방법

이 애플리케이션은 특정 MQTT 토픽으로 Protobuf 형식의 메시지를 수신합니다.

### 3.1. 토픽 구조
메시지를 발행할 토픽은 `MqttTopic.java`에 정의된 규칙을 따릅니다. 기본 구조는 다음과 같습니다:
`<정보_유형>/<경로_지점>/<인바운드_아웃바운드>`
예시: `obe/tbus/inb`

애플리케이션은 이 토픽 뒤에 `/`와 추가적인 서브 토픽 레벨(예: `obe/tbus/inb/device123`)이 붙는 메시지를 구독합니다.

### 3.2. 메시지 형식 (Protobuf)
메시지 페이로드는 `src/main/proto/bms.proto`에 정의된 `Location` Protobuf 메시지 형식이어야 합니다.

```protobuf
// bms.proto 예시
syntax = "proto3";

option java_package = "tmoney.gbi.bms.proto";
option java_outer_classname = "Location";

message Location {
  string device_id = 1;
  double latitude = 2;
  double longitude = 3;
  // ... 기타 필드
}
```

### 3.3. 메시지 발행 방법

1.  **MQTT 클라이언트 사용:**
    *   Mosquitto `mosquitto_pub` 명령줄 도구 또는 MQTT Explorer와 같은 GUI 클라이언트를 사용할 수 있습니다.
    *   `bms.proto`를 사용하여 `Location` 메시지를 Protobuf 바이너리 형식으로 직렬화해야 합니다.
    *   예시 (Python을 사용하여 Protobuf 메시지 생성 및 발행):

        ```python
        # pip install paho-mqtt protobuf
        import paho.mqtt.publish as publish
        from google.protobuf.timestamp_pb2 import Timestamp
        from bms_pb2 import Location # bms.proto를 컴파일하여 생성된 파일

        # Location 메시지 생성
        location_msg = Location()
        location_msg.device_id = "test-device-001"
        location_msg.latitude = 37.5665
        location_msg.longitude = 126.9780
        location_msg.last_stop_id.value = "vehicle-A" # 예시 필드

        # 현재 시간 타임스탬프 추가 (occur_at 필드가 있다면)
        timestamp = Timestamp()
        timestamp.GetCurrentTime()
        location_msg.occur_at.CopyFrom(timestamp)


        # Protobuf 메시지를 바이트로 직렬화
        payload = location_msg.SerializeToString()

        # MQTT 메시지 발행
        broker_address = "localhost" # 또는 MQTT 브로커의 IP 주소
        topic = "obe/tbus/inb/device-data" # 애플리케이션이 구독하는 토픽

        print(f"Publishing to topic: {topic}")
        print(f"Payload (bytes): {payload}")

        publish.single(topic, payload, hostname=broker_address)
        print("Message published.")
        ```
        (참고: `bms_pb2.py` 파일은 `protoc --python_out=. bms.proto` 명령으로 생성할 수 있습니다.)

2.  **샘플 HTTP 요청 사용 (개발/테스트용):**
    *   `bmsPublisher.http` 파일은 IntelliJ IDEA의 HTTP 클라이언트와 같은 도구를 사용하여 샘플 MQTT 메시지를 발행하는 방법을 보여줍니다.
    *   이 요청은 내부적으로 `MqttProtobufTestController`를 호출하여 미리 정의된 Protobuf 메시지를 MQTT 브로커로 발행합니다.
    *   이 방법은 실제 MQTT 클라이언트를 설정하지 않고도 애플리케이션의 메시지 처리 흐름을 테스트하는 데 유용합니다.
