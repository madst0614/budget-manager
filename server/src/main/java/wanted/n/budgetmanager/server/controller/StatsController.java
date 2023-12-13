package wanted.n.budgetmanager.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.n.budgetmanager.server.dto.*;
import wanted.n.budgetmanager.server.service.AuthService;
import wanted.n.budgetmanager.server.service.StatsService;

import java.time.LocalDate;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RequestMapping("/api/v1/stats")
@Api(tags = "Statistics API", description = "통계와 관련된 API")
@RestController
public class StatsController {
    private final AuthService authService;
    private final StatsService statsService;
    @GetMapping("/rates/lastMonth")
    @ApiOperation(value = "사용자 지난 달 대비 소비율 가져오기", notes = "사용자의 지난 달 대비 소비율 통계를 가져옵니다.")
    public ResponseEntity<StatsLastMonthTodaySpdRateResponseDTO> getLastMonthSpdRate(
            @RequestHeader(AUTHORIZATION) String token) {

        // 토큰으로부터 id 추출
        Long userId = authService.getUserIdFromToken(token);
        LocalDate now = LocalDate.now();

        return ResponseEntity.status(OK).body
                (statsService.getStatsLastMonthTodaySpdRate(StatsLastMonthTodaySpdRateRequestDTO
                        .builder().userId(userId).now(now).build()));
    }

    @GetMapping("/rates/lastWeek")
    @ApiOperation(value = "사용자 지난 주 대비 소비율 가져오기", notes = "사용자의 지난 주 대비 소비율 통계를 가져옵니다.")
    public ResponseEntity<StatsLastWeekTodaySpdRateResponseDTO> getLastWeekSpdRate(
            @RequestHeader(AUTHORIZATION) String token) {

        // 토큰으로부터 id 추출
        Long userId = authService.getUserIdFromToken(token);
        LocalDate now = LocalDate.now();

        return ResponseEntity.status(OK).body
                (statsService.getStatsLastWeekTodaySpdRate(StatsLastWeekTodaySpdRateRequestDTO
                        .builder().userId(userId).now(now).build()));
    }

    @GetMapping("/rates/otherUsers-Me")
    @ApiOperation(value = "사용자 다른 유저 대비 소비율 가져오기", notes = "사용자의 다른 유저 대비 예산 현재 소비율 통계를 가져옵니다.")
    public ResponseEntity<StatsOthersMeSpdRateResponseDTO> getOtherUsersMeBudgetSpdRate(
            @RequestHeader(AUTHORIZATION) String token) {

        // 토큰으로부터 id 추출
        Long userId = authService.getUserIdFromToken(token);
        LocalDate now = LocalDate.now();

        return ResponseEntity.status(OK).body
                (statsService.getStatsOthersMeSpdRate(StatsOthersMeSpdRateRequestDTO
                        .builder().userId(userId).now(now).build()));
    }

    @GetMapping("budgetdetail/average")
    @ApiOperation(value = "모든 사용자 예산 평균 가져오기", notes = "모든 사용자의 예산 평균 퍼센테이지를 가져옵니다.")
    public ResponseEntity<StatsBudgetDetailAverageResponseDTO> getStatsBudgetDetailAverage(
            @RequestHeader(AUTHORIZATION) String token) {

        return ResponseEntity.status(OK).body
                (statsService.getStatsBudgetDetailAverage());
    }

}


