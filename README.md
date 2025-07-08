# Mosquitto MQTT 브로커 Windows 설치 및 설정 가이드

이 가이드는 Windows 환경에서 Mosquitto MQTT 브로커를 설치하고 설정하는 단계별 지침을 제공합니다. 인증 설정과 구독 예제도 포함되어 있습니다.

## 1. Mosquitto 설치 (Windows)

### 설치 단계
1. Mosquitto 설치 파일을 다운로드합니다:
   - 파일: `./file/mosquitto-2.0.21a-install-windows-x64.exe`
2. 설치 파일을 실행합니다.
3. 기본 설정으로 설치를 진행합니다. 일반적으로 `C:\Program Files\mosquitto` 경로에 설치됩니다.
4. 설치 중 'Service' 관련 옵션이 나타나면, 수동 실행 및 테스트를 위해 체크를 해제하는 것이 편리할 수 있습니다.
5. **선택 사항**: `mosquitto`, `mosquitto_pub`, `mosquitto_sub` 명령어를 어느 위치에서든 사용할 수 있도록 환경 변수에 Mosquitto 경로를 추가합니다.
   - **시스템 속성 > 고급 > 환경 변수**로 이동합니다.
   - `Path` 변수에 `C:\Program Files\mosquitto`를 추가합니다.

## 2. 인증 설정

MQTT 브로커에 아무나 접속하지 못하도록 사용자 이름과 비밀번호를 설정합니다.

### 2.1. 비밀번호 파일 생성
1. 명�� 프롬프트(cmd) 또는 PowerShell을 엽니다.
2. Mosquitto 설치 폴더로 이동합니다:
   ```bash
   cd "C:\Program Files\mosquitto"
   ```
3. `mosquitto_passwd.exe` 유틸리티를 사용하여 비밀번호 파일을 생성합니다:
   - 사용자: `mqtt_user`
   - 비밀번호: `mqtt_password`
   - 다음 명령어를 실행하고 `mqtt_password`를 두 번 입력합니다:
     ```bash
     mosquitto_passwd.exe -c passwordfile.txt mqtt_user
     ```
   - `passwordfile.txt` 파일이 `C:\Program Files\mosquitto` 폴더에 생성됩니다.

### 2.2. 설정 파일 (`mosquitto.conf`) 작성
1. `C:\Program Files\mosquitto` 폴더에 `mosquitto.conf`라는 이름으로 새 텍스트 파일을 만듭니다.
2. 다음 내용을 `mosquitto.conf` 파일에 복사하여 붙여넣습니다:

   ```text
   # =================================================================
   # 일반 설정
   # =================================================================
   # 익명 접속을 허용하지 않음
   allow_anonymous false

   # 비밀번호 파일의 절대 경로 지정
   password_file "C:\Program Files\mosquitto\passwordfile.txt"

   # =================================================================
   # 리스너 설정
   # =================================================================
   # 기�� MQTT 포트 설정
   listener 1883
   protocol mqtt
   ```

   **참고**: `password_file` 경로는 `passwordfile.txt` 파일의 실제 위치와 정확히 일치해야 합니다.

## 3. Mosquitto 브로커 실행
1. 명령 프롬프트를 엽니다.
2. Mosquitto 설치 폴더로 이동합니다:
   ```bash
   cd "C:\Program Files\mosquitto"
   ```
3. 작성한 `mosquitto.conf` 설정 파일을 사용하여 브로커를 실행합니다. `-v` 옵션은 자세한 로그를 출력하여 연결 상태를 확인하는 데 유용합니다:
   ```bash
   mosquitto.exe -c mosquitto.conf -v
   ```
4. 브로커가 정상적으로 실행되면 명령 프롬프트 창에서 로그를 출력하며 활성 상태를 유지합니다.

## 4. MQTT 구독 예제
브로커가 실행 중인 상태에서 특정 토픽을 구독하여 테스트할 수 있습니다.

1. 브로커가 실행 중인 상태에서 새 명령 프롬프트 창을 엽니다.
2. 다음 명령어를 실행하여 토픽을 구독합니다:
   - 호스트: `localhost`
   - 포트: `1883`
   - 사용자 이름: `mqtt_user`
   - 비밀번호: `mqtt_password`
   - 클라이언트 ID: `bms-subscriber-12345` (고유한 ID 사용)
   - 토픽: `test/topic` (예시 토픽)

   ```bash
   mosquitto_sub -h localhost -p 1883 -u "mqtt_user" -P "mqtt_password" -i "bms-subscriber-12345" -t "test/topic" -d
   ```

   **명령어 옵션**:
   - `-h`: 호스트 주소
   - `-p`: 포트 번호
   - `-u`: 사용자 이름
   - `-P`: 비밀번호
   - `-i`: 클라이언트 ID
   - `-t`: 구독할 토픽
   - `-d`: 디버그 메시지 출력

3. 다른 클라이언트에서 `test/topic`으로 메시지를 발행하면, 이 구독 클라이언트 창에 해당 메시지가 표시됩니다.

---

# 프로젝트 아키텍처 및 워크플로우

이 문서는 MQTT 메시지를 수신하여 데이터베이스에 저장하기까지의 전체 과정을 설명합니다. 시스템은 **전략 패턴(Strategy Pattern)**을 기반으로 설계되어, 다양한 데이터 유형을 유연하고 확장 가능하게 처리할 수 있습니다.

## 핵심 워크플로우

메시지 처리 흐름은 다음 5단계로 이루어집니다.

1.  **메시지 수신 (Message Reception)**: MQTT 브로커로부터 메시지를 수신합니다.
2.  **라우팅 및 큐잉 (Routing & Queueing)**: 수신된 메시지를 토픽(Topic)에 따라 적절한 메모리 큐(Queue)에 저장합니다.
3.  **배치 프로세서 실행 (Batch Processing)**: 백그라운드 스레드에서 주기적으로 큐의 메시지를 가져와 일괄 처리를 시작합니다.
4.  **데이터 처리 (Data-Specific Processing)**: 전략 패턴을 사용하여 토픽에 맞는 데이터 처리기(DataProcessor)를 선택하고, 데이터 변환, 암호화 등 필요한 로직을 수행합니다.
5.  **데이터베이스 저장 (Database Insertion)**: 처리된 데이터를 데이터베이스에 일괄 삽입(Batch Insert)합니다.

![Workflow Diagram](https://user-images.githubusercontent.com/12572126/182925368-7a44c1e3-1a6a-47d9-8a4a-5e3b7e6e7e4e.png)  *<-- 다이어그램 예시입니다. 실제 프로젝트 구조에 맞게 수정이 필요할 수 있습니다.*

## 주요 컴포넌트 및 코드 예시

### 1. 메시지 수신 및 라우팅 (`MqttConfig` & `TopicMessageRouter`)

-   **`MqttConfig`**: MQTT 브로커 연결 및 메시지 리스너를 설정합니다. 메시지가 도착하면 `@ServiceActivator` 어노테이션이 붙은 메소드가 호출됩니다.
-   **`TopicMessageRouter`**: 수신된 메시지를 처리할 수 있는 핸들러(`MessageHandler`)에게 전달합니다.

```java
// In: MqttConfig.java
@Bean
@ServiceActivator(inputChannel = "mqttInputChannel")
public MessageHandler handler(TopicMessageRouter<byte[]> router) {
    return message -> {
        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        byte[] payload = (byte[]) message.getPayload();
        router.route(topic, payload);
    };
}
```

### 2. 메시지 큐잉 (`QueueingMessageHandler` & `QueueFactory`)

-   **`QueueingMessageHandler`**: `TopicMessageRouter`로부터 메시지를 받아, 토픽에 맞는 큐에 메시지를 넣습니다.
-   **`QueueFactory`**: 토픽의 앞 3개 레벨(예: `obe/tbus/inb`)을 기준으로 큐를 생성하고 관리합니다. 이를 통해 동일한 그룹의 토픽 메시지는 같은 큐에 쌓이게 됩니다.

```java
// In: handler/impl/QueueingMessageHandler.java
@Override
public void handle(String topic, byte[] message) {
    // 토픽에 맞는 큐를 찾아 메시지를 offer 합니다.
    queueFactory.getQueue(topic).offer(message);
}

// In: config/queue/QueueFactory.java
public BlockingQueue<byte[]> getQueue(String topic) {
    // "obe/tbus/inb/12345" -> "obe/tbus/inb" 키를 추출하여 큐를 찾습니다.
    String queueName = extractQueueName(topic);
    return queues.computeIfAbsent(queueName, k -> new LinkedBlockingQueue<>());
}
```

### 3. 배치 프로세서 실행 (`ExceuteProcess`)

-   애플리케이션이 시작되면 `ExceuteProcess`는 `MqttTopicConstants`에 정의된 모든 토픽 접두사에 대해 배치 처리 스레드를 생성하고 실행합니다.
-   `ProcessorProvider`를 통해 각 토픽 그룹을 처리할 `DataProcessor` (전략)를 찾아 `BatchProcessorRunnable`에 주입합니다.

```java
// In: config/ExceuteProcess.java
@Override
public void startBatchProcessors() {
    List<String> topicPrefixes = MqttTopicConstants.TOPIC_PREFIXES;

    for (String topicPrefix : topicPrefixes) {
        // 1. 토픽에 맞는 프로세서(전략)를 찾습니다.
        DataProcessor<?> dataProcessor = processorProvider.getProcessor(topicPrefix);
        if (dataProcessor == null) continue;

        // 2. 스레드 수만큼 범용 Runnable을 생성하여 실행합니다.
        for (int i = 0; i < threadCount; i++) {
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
```

### 4. 데이터 처리 (전략 패턴: `DataProcessor` & `LocationDataProcessor`)

-   **`DataProcessor<T>`**: 데이터 처리 전략에 대한 인터페이스입니다. 데이터 변환, 처리, DB 저장을 정의합니다.
-   **`LocationDataProcessor`**: `EncryptedLocationDto` 타입의 데이터를 처리하는 구체적인 전략 클래스입니다.

```java
// In: config/processor/DataProcessor.java (Interface)
public interface DataProcessor<T> {
    boolean supports(String topicPrefix); // 어떤 토픽을 지원하는지
    T convert(byte[] message) throws Exception; // 데이터 변환
    void process(T dto); // 데이터 처리 (예: 암호화)
    void batchInsert(List<T> batch); // DB 저장
}

// In: config/processor/LocationDataProcessor.java (Implementation)
@Component
@RequiredArgsConstructor
public class LocationDataProcessor implements DataProcessor<EncryptedLocationDto> {
    // ... 의존성 주입 ...

    @Override
    public boolean supports(String topicPrefix) {
        // 이 프로세서는 모든 토픽을 지원한다고 가정
        return true;
    }
    // ... convert, process, batchInsert 메소드 구현 ...
}
```

### 5. 범용 배치 실행 및 DB 저장 (`BatchProcessorRunnable`)

-   `BatchProcessorRunnable`은 특정 데이터 타입에 종속되지 않는 범용 실행자입니다.
-   주기적으로 큐를 확인(`poll`)하고, 메시지가 있으면 주입된 `DataProcessor` 전략을 사용하여 변환, 처리, 배치를 구성합니다.
-   배치가 가득 차거나 타임아웃이 발생하면 `dataProcessor.batchInsert()`를 호출하여 DB에 최종 저장합니다.

```java
// In: config/processor/BatchProcessorRunnable.java
@Override
public void run() {
    while (!Thread.currentThread().isInterrupted()) {
        // ... 큐에서 메시지를 꺼내고 배치 리스트(batchList)를 만듭니다 ...
        
        T dto = dataProcessor.convert(message); // 1. 변환 (전략 위임)
        dataProcessor.process(dto);             // 2. 처리 (전략 위임)
        batchList.add(dto);

        if (batchList.size() >= batchSize) {
            processBatch(batchList); // 3. DB 저장 로직 호출
        }
    }
}

private void processBatch(List<T> batchList) {
    // ...
    dataProcessor.batchInsert(batchList); // 4. 최종 저장 (전략 위임)
    // ...
}
```

## 새로운 데이터 타입 추가 방법

이 아키텍처의 가장 큰 장점은 확장성입니다. 새로운 종류의 메시지(예: `StatusDto`)를 처리하려면 다음 두 단계만 수행하면 됩니다.

1.  **새로운 DTO와 Mapper 메소드 생성**:
    -   `StatusDto.java` 모델을 생성합니다.
    -   `CommonInsertMapper.java` 인터페이스와 `common-insert-mapper.xml`에 `insertStatusBatch`와 같은 새로운 배치 삽입 메소드를 추가합니다.

2.  **새로운 `DataProcessor` 구현체 생성**:
    -   `DataProcessor<StatusDto>`를 구현하는 `StatusDataProcessor.java` 클래스��� 새로 만듭니다.
    -   `supports()` 메소드에 이 프로세서가 처리할 토픽(예: `topicPrefix.startsWith("status/")`)을 명시합니다.
    -   `convert`, `process`, `batchInsert` 메소드를 `StatusDto`에 맞게 구현합니다.

```java
// 예시: StatusDataProcessor.java
@Component
@RequiredArgsConstructor
public class StatusDataProcessor implements DataProcessor<StatusDto> {
    // ... Status 관련 의존성 주입 ...

    @Override
    public boolean supports(String topicPrefix) {
        // "status/"로 시작하는 토픽만 처리
        return topicPrefix.startsWith("status/");
    }

    @Override
    public StatusDto convert(byte[] message) throws Exception {
        // byte[] -> StatusDto 변환 로직
    }

    @Override
    public void process(StatusDto dto) {
        // StatusDto에 필요한 처리 (없으면 비워둠)
    }

    @Override
    public void batchInsert(List<StatusDto> batch) {
        commonInsertMapper.insertStatusBatch(batch);
    }
}
```

위와 같이 클래스를 추가하고 애플리케이션을 재시작하면, 별도의 설정 변경 없이 "status/"로 시작하는 모든 토픽의 메시지가 자동으로 처리됩니다.
