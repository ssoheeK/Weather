package zerobase.weather.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.openapi.OpenApi;
import zerobase.weather.repository.DateWeatherRepository;

@Slf4j
@Component
@AllArgsConstructor
public class WeatherScheduler {

    private final OpenApi openApi;
    private final DateWeatherRepository dateWeatherRepository;

    @Transactional
    @Scheduled(cron = "${scheduler.api.openweathermap}")
    public void weatherScheduling() {
        log.info("get openApi weather scheduler is started");
        var dateWeather = this.openApi.weatherOpenApi();

        log.info("insert new weather -> " + dateWeather.toString());
        this.dateWeatherRepository.save(dateWeather);
    }
}
