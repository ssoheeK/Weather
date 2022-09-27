package zerobase.weather.openapi;

import lombok.var;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import zerobase.weather.domain.DateWeather;

import java.time.LocalDate;

@Component
public class WeatherOpenApi implements OpenApi {

    @Value("${openweathermap.key}")
    private String apiKey;

    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather";

    @Override
    public DateWeather weatherOpenApi() {
        var dateWeather = new DateWeather();
        dateWeather.setDate(LocalDate.now());

        try {
            // api 요청
            WebClient webClient = WebClient.builder().baseUrl(API_URL).build();
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("q", "seoul")
                            .queryParam("appid", apiKey)
                            .build()
                    ).retrieve().bodyToMono(String.class).block();

            // 파싱 시작
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response);

            JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
            JSONObject weatherData = (JSONObject) weatherArray.get(0);
            JSONObject mainData = (JSONObject) jsonObject.get("main");

            // 데이터 세팅
            dateWeather.setDate(LocalDate.now());
            dateWeather.setWeather(weatherData.get("main").toString());
            dateWeather.setIcon(weatherData.get("icon").toString());
            dateWeather.setTemperature((Double) mainData.get("temp"));

        } catch (ParseException e) {
            //TODO
            throw new RuntimeException(e);
        }

        return dateWeather;
    }

}
