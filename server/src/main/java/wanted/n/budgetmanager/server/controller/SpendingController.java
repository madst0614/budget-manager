package wanted.n.budgetmanager.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.n.budgetmanager.server.dto.*;
import wanted.n.budgetmanager.server.service.AuthService;
import wanted.n.budgetmanager.server.service.SpendingService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RequestMapping("/api/v1/spendings")
@Api(tags = "Spending API", description = "지출과 관련된 API")
@RestController
public class SpendingController {
    private final SpendingService spendingService;
    private final AuthService authService;

    @GetMapping("")
    @ApiOperation(value = "지출 목록 가져오기", notes = "사용자의 지출 목록을 가져옵니다.")
    public ResponseEntity<SpendingListResponseDTO> getSpendingList(
            @RequestHeader(AUTHORIZATION) String token,
            @RequestParam(value = "categoryList") List<Long> categoryList,
            @RequestParam(value = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(value = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  end,
            @RequestParam(value = "min") Integer min,
            @RequestParam(value = "max") Integer max) {

        // 토큰으로부터 id 추출
        Long userId = authService.getUserIdFromToken(token);
        return ResponseEntity.status(OK).body
                (spendingService.getSpendingList(SpendingListDTO.builder()
                                .userId(userId)
                                .categoryList(categoryList)
                                .start(start)
                                .end(end)
                                .min(min)
                                .max(max)
                                .build()));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "지출 기록 가져오기", notes = "사용자가 선택한 지출 기록을 가져옵니다.")
    public ResponseEntity<SpendingDetailResponseDTO> getSpendingDetail(
            @RequestHeader(AUTHORIZATION) String token,
            @PathVariable("id") Long id) {

        Long userId = authService.getUserIdFromToken(token);

        return ResponseEntity.status(OK).body
                (spendingService.getSpendingDetail
                        (SpendingDetailRequestDTO
                                .builder()
                                .userId(userId)
                                .id(id)
                                .build()));
    }

    @PostMapping("")
    @ApiOperation(value = "지출 기록 생성", notes = "사용자의 지출 기록을 생성합니다.")
    public ResponseEntity<Void> createSpending(
            @RequestHeader(AUTHORIZATION) String token,
            @Valid @RequestBody SpendingCreateRequestDTO spendingCreateRequestDTO ) {

        // 토큰으로부터 userid 추출
        Long userId = authService.getUserIdFromToken(token);

        spendingService.createSpending(SpendingCreateDTO.builder()
                        .userId(userId)
                        .catId(spendingCreateRequestDTO.getCatId())
                        .date(spendingCreateRequestDTO.getDate())
                        .amount(spendingCreateRequestDTO.getAmount())
                        .memo(spendingCreateRequestDTO.getMemo())
                        .excluded(spendingCreateRequestDTO.getExcluded())
                        .build());

        return ResponseEntity.status(CREATED).build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value="지출 기록 삭제", notes="사용자의 지출 기록을 삭제합니다.")
    public ResponseEntity<Void> deleteSpending(@RequestHeader(AUTHORIZATION) String token,
                                             @PathVariable("id") Long id){
        // 토큰으로부터 userid 추출
        Long userId = authService.getUserIdFromToken(token);

        spendingService.deleteSpending(SpendingDeleteDTO.builder().userId(userId).id(id).build());

        return ResponseEntity.status(OK).build();
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "지출 기록 업데이트", notes = "사용자의 지출 기록을 업데이트합니다.")
    public ResponseEntity<Void> updateBudget(
            @RequestHeader(AUTHORIZATION) String token,
            @PathVariable("id") Long id,
            @Valid @RequestBody SpendingUpdateRequestDTO spendingUpdateRequestDTO ) {

        // 토큰으로부터 userid 추출
        Long userId = authService.getUserIdFromToken(token);

        spendingService.updateSpending(SpendingUpdateDTO.builder()
                        .userId(userId)
                        .id(id)
                        .catId(spendingUpdateRequestDTO.getCatId())
                        .date(spendingUpdateRequestDTO.getDate())
                        .amount(spendingUpdateRequestDTO.getAmount())
                        .memo(spendingUpdateRequestDTO.getMemo())
                        .excluded(spendingUpdateRequestDTO.getExcluded())
                .build());

        return ResponseEntity.status(OK).build();
    }
}
