package com.algaworks.algasensors.temperature.monitoring.domain.service.impl;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import com.algaworks.algasensors.temperature.monitoring.domain.service.SensorAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorAlertServiceImpl implements SensorAlertService {

    private final SensorAlertRepository repository;

    @Override
    public void handleAlert(TemperatureLogData data) {
        repository.findById(new SensorId(data.getSensorId()))
                .ifPresentOrElse(
                        alert -> {
                            if (alert.getMaxTemperature() != null && data.getValue().compareTo(alert.getMaxTemperature()) >= 0) {
                                logIgnoredAlert("Alert Max Temp: SensorId {} Temp {}", data);

                            } else if (alert.getMinTemperature() != null && data.getValue().compareTo(alert.getMinTemperature()) <= 0) {
                                logIgnoredAlert("Alert Min Temp: SensorId {} Temp {}", data);
                            } else {
                                logIgnoredAlert("Alert Ignored: SensorId {} Temp {}", data);
                            }
                        }, () -> {
                            logIgnoredAlert("Alert Ignored: SensorId {} Temp {}", data);
                        }
                );
    }

    private static void logIgnoredAlert(String format, TemperatureLogData data) {
        log.info(format,
                data.getSensorId(), data.getValue());
    }
}
