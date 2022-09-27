package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.openapi.OpenApi;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
@Slf4j
public class DiaryService {

    private final DiaryRepository diaryRepository;

    private final DateWeatherRepository dateWeatherRepository;

    private final OpenApi openApi;


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Diary createDiary(LocalDate date, String text) {
        log.info("started to create diary");
        // 날씨 데이터 가져오기
        DateWeather dateWeather = getDateWeather(date);

        // DB에 저장
        Diary nowDiary = new Diary();
        nowDiary.setDateWeather(dateWeather);
        nowDiary.setText(text);
        var diary = diaryRepository.save(nowDiary);
        log.info("end to create diary");
        return diary;
    }

    private DateWeather getDateWeather(LocalDate date) {
        List<DateWeather> dateWeatherListFromDB = dateWeatherRepository.findAllByDate(date);
        if (dateWeatherListFromDB.size() == 0) {
            // 새로 api에서 날씨 정보를 가져와야 한다.
            // 정책상 현재 날씨를 가져오도록 하거나 날씨없이 일기 쓰도록
            return this.openApi.weatherOpenApi();
        } else {
            return dateWeatherListFromDB.get(0);
        }
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
        /*if (date.isAfter(LocalDate.ofYearDay(3050, 1))) {
            throw new InvalidDate();
        }*/
        return diaryRepository.findAllByDate(date);
    }
    @Transactional(readOnly = true)
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    public void updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        nowDiary.setText(text);

        diaryRepository.save(nowDiary);
    }

    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }
}
