package wanted.n.budgetmanager.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.n.budgetmanager.server.dto.*;
import wanted.n.budgetmanager.server.service.AuthService;
import wanted.n.budgetmanager.server.service.BudgetService;

import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RequestMapping("/api/v1/budgets")
@Api(tags = "Budget API", description = "예산과 관련된 API")
@RestController
public class BudgetController {
    private final BudgetService budgetService;
    private final AuthService authService;

    @GetMapping("")
    @ApiOperation(value = "예산 가져오기", notes = "사용자가 설정한 예산 정보들을 가져옵니다.")
    public ResponseEntity<BudgetListResponseDTO> getBudgetList(
            @RequestHeader(AUTHORIZATION) String token) {

        // 토큰으로부터 id 추출하여 budgetListDTO에 넣음
        return ResponseEntity.status(OK).body
                (budgetService.getBudgetList
                        (BudgetListDTO
                                .builder()
                                .userId(authService.getUserIdFromToken(token))
                                .build()));
    }

    @GetMapping("/{bgId}")
    @ApiOperation(value = "예산 디테일 가져오기", notes = "사용자가 선택한 예산 디테일을 가져옵니다.")
    public ResponseEntity<BudgetDetailResponseDTO> getBudgetDetail(
            @RequestHeader(AUTHORIZATION) String token,
            @PathVariable("bgId") Long bgId) {

        Long userId = authService.getUserIdFromToken(token);

        return ResponseEntity.status(OK).body
                (budgetService.getBudgetDetail
                        (BudgetDetailDTO
                                .builder()
                                .userId(userId)
                                .bgId(bgId)
                                .build()));
    }

    @PostMapping("")
    @ApiOperation(value = "예산 등록", notes = "사용자가 만든 예산을 등록합니다.")
    public ResponseEntity<Void> registerBudget(
            @RequestHeader(AUTHORIZATION) String token,
            @Valid @RequestBody BudgetRegisterRequestDTO budgetRegisterRequestDTO ) {

        // 토큰으로부터 userid 추출
        Long userId = authService.getUserIdFromToken(token);

        budgetService.registerBudget(BudgetRegisterDTO.from(userId, budgetRegisterRequestDTO));

        return ResponseEntity.status(CREATED).build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value="예산 삭제", notes="사용자가 만든 예산을 삭제합니다.")
    public ResponseEntity<Void> deleteBudget(@RequestHeader(AUTHORIZATION) String token,
                    @PathVariable("id") Long id){
        // 토큰으로부터 userid 추출
        Long userId = authService.getUserIdFromToken(token);

        budgetService.deleteBudget(BudgetDeleteDTO.builder().userId(userId).id(id).build());

        return ResponseEntity.status(OK).build();
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "예산 업데이트", notes = "사용자가 만든 예산을 업데이트 합니다.")
    public ResponseEntity<Void> updateBudget(
            @RequestHeader(AUTHORIZATION) String token,
            @PathVariable("id") Long id,
            @Valid @RequestBody BudgetUpdateRequestDTO budgetUpdateRequestDTO ) {

        // 토큰으로부터 userid 추출
        Long userId = authService.getUserIdFromToken(token);

        BudgetUpdateDTO budgetUpdateDTO = BudgetUpdateDTO.from(userId, budgetUpdateRequestDTO);

        budgetUpdateDTO.setId(id);

        budgetService.updateBudget(budgetUpdateDTO);

        return ResponseEntity.status(OK).build();
    }

    @PutMapping("detail/{bgId}")
    @ApiOperation(value = "예산 디테일 업데이트", notes = "사용자가 설정한 예산 디테일을 업데이트 합니다.")
    public ResponseEntity<Void> updateBudgetDetail(
            @RequestHeader(AUTHORIZATION) String token,
            @PathVariable("bgId") Long bgId,
            @Valid @RequestBody BudgetDetailUpdateRequestDTO budgetDetailUpdateRequestDTO ) {

        // 토큰으로부터 userid 추출
        Long userId = authService.getUserIdFromToken(token);

        BudgetDetailUpdateDTO budgetDetailUpdateDTO = BudgetDetailUpdateDTO.from(userId, budgetDetailUpdateRequestDTO);
        // bgid 추가
        budgetDetailUpdateDTO.setBgId(bgId);

        budgetService.updateBudgetDetail(budgetDetailUpdateDTO);

        return ResponseEntity.status(OK).build();
    }
}
