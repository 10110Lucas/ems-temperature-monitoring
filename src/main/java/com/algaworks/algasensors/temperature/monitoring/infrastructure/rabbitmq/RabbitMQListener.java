package com.algaworks.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

import static com.algaworks.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    @SneakyThrows
    @RabbitListener(queues = QUEUE)
    public void handle(@Payload TemperatureLogData data, @Headers Map<String, Object> headers) {
        TSID sensorId = data.getSensorId();
        Double value = data.getValue();

        log.info("Headers: {}", headers);
        log.info("Temperature updated: SensorId {} Temp {}", sensorId, value);

        Thread.sleep(Duration.ofSeconds(5));
    }
}
