package zerobase.weather.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.var;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.domain.Diary;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @ApiOperation(value = "일기 텍스트와 날씨를 이용해서 DB에 일기 저장", notes = "일기 텍스트와 날씨를 이용해서 DB에 일기 저장")
    @PostMapping("/create/diary")
    public ResponseEntity<?> createDiary(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date
            , @RequestBody String text) {
        var diary = diaryService.createDiary(date, text);
        return ResponseEntity.ok(diary);
    }

    @ApiOperation("선택한 날짜의 모든 일기 데이터를 가져옵니다.")
    @GetMapping("/read/diary")
    public ResponseEntity<?> readDiary(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
                          @ApiParam(value = "날짜 형식 : yyyy-MM-dd", example = "2020-02-02") LocalDate date) {
        var diaries = diaryService.readDiary(date);
        return ResponseEntity.ok(diaries);
    }

    @ApiOperation("선택한 기간 중의 모든 일기 데이터를 가져옵니다.")
    @GetMapping("/read/diaries")
    public ResponseEntity<?> readDiaries(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        var diaries = diaryService.readDiaries(startDate, endDate);
        return ResponseEntity.ok(diaries);
    }

    @ApiOperation("선택한 날짜의 일기 데이터를 수정합니다.")
    @PutMapping("/update/diary")
    public void updateDiary(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date,
                                             @RequestBody String text) {
        diaryService.updateDiary(date, text);
    }

    @ApiOperation("선택한 날짜의 일기 데이터를 삭제합니다.")
    @DeleteMapping("/delete/diary")
    public void deleteDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        diaryService.deleteDiary(date);
    }
}
