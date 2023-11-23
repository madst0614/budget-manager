package wanted.n.budgetmanager.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.n.budgetmanager.server.domain.Budget;
import wanted.n.budgetmanager.server.domain.BudgetDetail;
import wanted.n.budgetmanager.server.dto.*;
import wanted.n.budgetmanager.server.exception.CustomException;
import wanted.n.budgetmanager.server.exception.ErrorCode;
import wanted.n.budgetmanager.server.repository.BudgetDetailRepository;
import wanted.n.budgetmanager.server.repository.BudgetRepository;
import wanted.n.budgetmanager.server.repository.mapping.UserId;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetService {
     private final BudgetRepository budgetRepository;
     private final BudgetDetailRepository budgetDetailRepository;

     /** 예산 조회 메소드
      *
      *   유저 ID로 저장된 예산 리스트를 가져옵니다
      */
     @Transactional
     public BudgetListResponseDTO getBudgetList(BudgetListDTO budgetListDTO){

          return BudgetListResponseDTO.builder()
                  .budgetList(budgetRepository.findAllByUserIdAndDeleted(budgetListDTO.getUserId(), false))
                  .build();
     }

     /** 예산 디테일 조회 메소드
      *
      *   선택한 예산 정보를 가져옵니다.
      */
     @Transactional
     public BudgetDetailResponseDTO getBudgetDetail(BudgetDetailDTO budgetDetailDTO){

          isUserAccessValid(budgetDetailDTO.getUserId(), budgetDetailDTO.getBgId());

          return BudgetDetailResponseDTO
                  .builder()
                  .budgetDetailList(budgetDetailRepository.findAllByBgId(budgetDetailDTO.getBgId()))
                  .build();
     }

     /** 예산 등록 메소드
          유효 기간인지 or not throw Exception
          사용자가 설정한 예산을 등록합니다.
      */
     @Transactional
     public void registerBudget(BudgetRegisterDTO budgetRegisterDTO){

          if(budgetRegisterDTO.getStart().isAfter(budgetRegisterDTO.getEnd()))
               throw new CustomException(ErrorCode.INVALID_DATE);

          budgetRepository.save(Budget.builder()
                          .userId(budgetRegisterDTO.getUserId())
                          .start(budgetRegisterDTO.getStart())
                          .end(budgetRegisterDTO.getEnd())
                          .deleted(false)
                          .build());
     }

     /** 예산 삭제 메소드
      *
      *   사용자 예산을 삭제합니다
      *   실제 삭제되지 않고 deleted = true 작업합니다.
      */
     @Transactional
     public void deleteBudget(BudgetDeleteDTO budgetDeleteDTO){
          Budget budget = budgetRepository.findById(budgetDeleteDTO.getId())
                  .orElseThrow(()->new CustomException(ErrorCode.BUDGET_NOT_FOUND));

          isUserAccessValid(budgetDeleteDTO.getUserId(), budget);

          Budget deletedBudget = Budget.builder()
                  .id(budget.getId())
                  .userId(budget.getUserId())
                  .start(budget.getStart())
                  .end(budget.getEnd())
                  .deleted(true)
                  .build();

          budgetRepository.save(deletedBudget);
     }

     /** 예산 설정 업데이트 메소드

         사용자 예산 설정을 바꿀 수 있습니다.
          예산이 DB에 있는지, user id가 일치하는지, 유효 기간인지 확인 or not throw exception
      */
     @Transactional
     public void updateBudget(BudgetUpdateDTO budgetUpdateDTO){
          // budget 없으면 throw BUDGET NOT FOUND
          Budget budget = budgetRepository.findById(budgetUpdateDTO.getId())
                  .orElseThrow(()->new CustomException(ErrorCode.BUDGET_NOT_FOUND));

          isUserAccessValid(budgetUpdateDTO.getUserId(), budget);

          if(budgetUpdateDTO.getStart().isAfter(budgetUpdateDTO.getEnd()))
               throw new CustomException(ErrorCode.INVALID_DATE);

          budgetRepository.save(Budget.builder()
                          .id(budget.getId())
                          .userId(budget.getUserId())
                          .start(budgetUpdateDTO.getStart())
                          .end(budgetUpdateDTO.getEnd())
                          .deleted(budget.getDeleted())
                          .build());
     }

     /** 예산 디테일 업데이트 메소드

          사용자의 예산 카테고리 별 금액을 수정합니다.
          예산존재, 삭제X, 소유자
          or not throw exception
      */
     @Transactional
     public void updateBudgetDetail(BudgetDetailUpdateDTO budgetDetailUpdateDTO){

          isUserAccessValid(budgetDetailUpdateDTO.getUserId(), budgetDetailUpdateDTO.getBgId());

          // budgetCatPctList 내 bgid 변조 가능성 있으므로 다시 작성
          List<BudgetDetail> budgetDetailList = new ArrayList<>();

          budgetDetailUpdateDTO.getBudgetDetailList()
                  .forEach(budgetDetail -> {
                       budgetDetailList
                          .add(BudgetDetail.builder()
                                  .bgId(budgetDetailUpdateDTO.getBgId())
                                  .catId(budgetDetail.getCatId())
                                  .amount(budgetDetail.getAmount())
                                  .build());});

          budgetDetailRepository.saveAll(budgetDetailList);
     }

     public void isUserAccessValid(Long accessUserId, Long bgId){
          UserId userId = budgetRepository.findUserIdById(bgId)
                  .orElseThrow(()-> new CustomException(ErrorCode.BUDGET_NOT_FOUND));

          if(userId.getDeleted())
               throw new CustomException(ErrorCode.BUDGET_DELETED);

          if(!userId.getUserId().equals(accessUserId)){
               throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
          }

     }

     public void isUserAccessValid(Long accessUserId, Budget budget){
          if(budget.getDeleted())
               throw new CustomException(ErrorCode.BUDGET_DELETED);

          if(!budget.getUserId().equals(accessUserId))
               throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
     }
}
