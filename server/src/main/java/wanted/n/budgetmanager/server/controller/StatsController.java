package wanted.n.budgetmanager.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.n.budgetmanager.server.dto.StatsSpdDayGetDTO;
import wanted.n.budgetmanager.server.dto.StatsSpdDayUpdateDTO;
import wanted.n.budgetmanager.server.dto.StatsSpdDayUpdateRequestDTO;
import wanted.n.budgetmanager.server.dto.StatsSpyDayResponseDTO;
import wanted.n.budgetmanager.server.service.AuthService;
import wanted.n.budgetmanager.server.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RequestMapping("/api/v1/stats")
@Api(tags = "Statistics API", description = "통계와 관련된 API")
@RestController
public class StatsController {
    private final AuthService authService;
    private final StatsService statsService;
    @GetMapping("{yyyy}/{MM}/{dd}")
    @ApiOperation(value = "사용자 하루 통계 가져오기", notes = "사용자 하루 통계를 가져옵니다.")
    public ResponseEntity<StatsSpyDayResponseDTO> getStatsSpdDay(
            @RequestHeader(AUTHORIZATION) String token,
            @PathVariable("yyyy") Integer year,
            @PathVariable("MM") Integer month,
            @PathVariable("dd") Integer day,
            @RequestParam(value = "categoryList") List<Long> categoryList) {

        // 토큰으로부터 id 추출
        Long userId = authService.getUserIdFromToken(token);

        return ResponseEntity.status(OK).body
                (statsService.getStatsSpdDay(StatsSpdDayGetDTO
                        .builder()
                                .userId(userId)
                                .date(LocalDate.of(year, month, day))
                                .catIdList(categoryList)
                        .build()));
    }

    @PutMapping("{yyyy}/{MM}/{dd}")
    @ApiOperation(value = "사용자 하루 통계 업데이트", notes = "사용자의 하루 통계를 업데이트합니다.")
    public ResponseEntity<Void> updateBudget(
            @RequestHeader(AUTHORIZATION) String token,
            @PathVariable("yyyy") Integer year,
            @PathVariable("MM") Integer month,
            @PathVariable("dd") Integer day,
            @Valid @RequestBody StatsSpdDayUpdateRequestDTO statsSpdDayUpdateRequestDTO ) {

        // 토큰으로부터 userid 추출
        Long userId = authService.getUserIdFromToken(token);

        statsService.updateStatsSpdDay(StatsSpdDayUpdateDTO
                .builder()
                        .userId(userId)
                        .catId(statsSpdDayUpdateRequestDTO.getCatId())
                        .date(LocalDate.of(year, month, day))
                        .amount(statsSpdDayUpdateRequestDTO.getAmount())
                .build());

        return ResponseEntity.status(OK).build();
    }
}


