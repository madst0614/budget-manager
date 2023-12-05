package wanted.n.budgetmanager.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.n.budgetmanager.server.dto.TodaySpdNoticeRequestDTO;
import wanted.n.budgetmanager.server.dto.TodaySpdNoticeResponseDTO;
import wanted.n.budgetmanager.server.dto.TodaySpdRecomRequestDTO;
import wanted.n.budgetmanager.server.dto.TodaySpdRecomResponseDTO;
import wanted.n.budgetmanager.server.service.AuthService;
import wanted.n.budgetmanager.server.service.ConsultingService;

import java.time.LocalDate;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RequestMapping("/api/v1/consultings")
@Api(tags = "Consulting API", description = "컨설팅과 관련된 API")
@RestController
public class ConsultingController {
    private final ConsultingService consultingService;
    private final AuthService authService;

    @GetMapping("/recommend/today")
    @ApiOperation(value = "오늘 지출 추천 가져오기", notes = "사용자의 오늘 지출 추천을 가져옵니다.")
    public ResponseEntity<TodaySpdRecomResponseDTO> getTodaySpendingRecommend(
            @RequestHeader(AUTHORIZATION) String token
    ){
        Long userId = authService.getUserIdFromToken(token);
        LocalDate now = LocalDate.now();

        return ResponseEntity.status(OK).body(consultingService.todaySpendingRecommend(TodaySpdRecomRequestDTO.builder()
                .userId(userId)
                .now(now)
                .build()));
    }

    @GetMapping("/notice/today")
    @ApiOperation(value = "오늘 지출 정보 가져오기", notes = "사용자의 오늘 지출 정보를 가져옵니다.")
    public ResponseEntity<TodaySpdNoticeResponseDTO> getTodaySpendingNotice(
            @RequestHeader(AUTHORIZATION) String token
    ){
        Long userId = authService.getUserIdFromToken(token);
        LocalDate now = LocalDate.now();

        return ResponseEntity.status(OK).body(consultingService.todaySpendingNotice(
                TodaySpdNoticeRequestDTO.builder()
                        .userId(userId).now(now)
                        .build()));
    }
}
