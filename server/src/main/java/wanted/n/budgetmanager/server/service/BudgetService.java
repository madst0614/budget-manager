package wanted.n.budgetmanager.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.n.budgetmanager.server.domain.Budget;
import wanted.n.budgetmanager.server.domain.BudgetDetail;
import wanted.n.budgetmanager.server.domain.Category;
import wanted.n.budgetmanager.server.dto.*;
import wanted.n.budgetmanager.server.exception.CustomException;
import wanted.n.budgetmanager.server.exception.ErrorCode;
import wanted.n.budgetmanager.server.repository.BudgetDetailRepository;
import wanted.n.budgetmanager.server.repository.BudgetRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetService {
     private final BudgetRepository budgetRepository;
     private final BudgetDetailRepository budgetDetailRepository;
     private final CategoryService categoryService;

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
          Budget budget = budgetRepository.findById(budgetDetailDTO.getBgId())
                  .orElseThrow(()->new CustomException(ErrorCode.BUDGET_NOT_FOUND));

          isUserAccessValid(budgetDetailDTO.getUserId(), budget);

          List<BudgetDetail> budgeDetailList = budgetDetailRepository.findAllByBgId(budgetDetailDTO.getBgId());

          return BudgetDetailResponseDTO
                  .builder()
                  .date(budget.getDate())
                  .budgetDetailList(BudgetDetailVO.from(budgeDetailList))
                  .build();
     }

     /** 예산 등록 메소드
          유효 기간인지 or not throw Exception
          사용자가 설정한 예산을 등록합니다.
      */
     @Transactional
     public void registerBudget(BudgetRegisterDTO budgetRegisterDTO){
          Long userId = budgetRegisterDTO.getUserId();
          LocalDate now = monthPattern(budgetRegisterDTO.getDate());

          Optional<Budget> opt = budgetRepository.findByUserIdAndDateAndDeleted(userId, now, false);

          if(opt.isPresent()){
               throw new CustomException(ErrorCode.BUDGET_EXISTS);
          }

          Budget budget = budgetRepository.save(Budget.builder()
                          .userId(budgetRegisterDTO.getUserId())
                          .date(now)
                          .deleted(false)
                          .build());

          List<BudgetDetailVO> budgetDetailVOList =
                  budgetRegisterDTO.getBudgetDetailVOList();

          budgetDetailVOList.sort((o1, o2) -> (int) (o1.getCatId() - o2.getCatId()));

          List<Category> categoryList = categoryService.getCategoryList();
          Set<Long> categorySet = categoryList.stream().map(Category::getId).collect(Collectors.toSet());
          int ci = 0;

          List<Long> emptyList = new ArrayList<>();

          for(BudgetDetailVO budgetDetailVO : budgetDetailVOList){
               isBudgetDetailValid(budgetDetailVO);

               if(!categorySet.contains(budgetDetailVO.getCatId()))
                    throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);

               while (ci < categoryList.size() &&
                       budgetDetailVO.getCatId() > categoryList.get(ci).getId()){
                    emptyList.add(categoryList.get(ci).getId());

                    ci++;
               }

               ci++;
          }

          for(Long catId : emptyList){
               budgetDetailVOList.add(BudgetDetailVO
                       .builder()
                       .catId(catId)
                       .amount(0L)
                       .build());
          }

          budgetDetailRepository.saveAll(BudgetDetail.from(budget.getId(), budgetDetailVOList));
     }

     /** 예산 삭제 메소드
      *
      *   사용자 예산을 삭제합니다
      *   실제 삭제되지 않고 deleted = true 작업합니다.
      */
     @Transactional
     public void deleteBudget(BudgetDeleteDTO budgetDeleteDTO){

          Budget budget = budgetRepository.findById(budgetDeleteDTO.getBdId())
                  .orElseThrow(()->new CustomException(ErrorCode.BUDGET_NOT_FOUND));

          isUserAccessValid(budgetDeleteDTO.getUserId(), budget);

          Budget deletedBudget = Budget.builder()
                  .id(budget.getId())
                  .userId(budget.getUserId())
                  .date(budget.getDate())
                  .deleted(true)
                  .build();

          budgetRepository.save(deletedBudget);
     }

     /** 예산 카테고리 별 금액 업데이트 메소드

         사용자의 예산 카테고리 별 금액을 업데이트 합니다.
          예산이 DB에 있는지, user id가 일치하는지, 유효 기간인지 확인 or not throw exception
      */
     @Transactional
     public void updateBudgetDetail(BudgetDetailUpdateDTO budgetDetailUpdateDTO){
          // budget 없으면 throw BUDGET NOT FOUND
          if(budgetDetailUpdateDTO.getBudgetDetailList().isEmpty())
               return;

          Budget budget = budgetRepository.findById(budgetDetailUpdateDTO.getBgId())
                  .orElseThrow(()->new CustomException(ErrorCode.BUDGET_NOT_FOUND));

          isUserAccessValid(budgetDetailUpdateDTO.getUserId(), budget);

          List<BudgetDetail> budgetDetailList = new ArrayList<>();

          List<Category> categoryList = categoryService.getCategoryList();
          Set<Long> categorySet = categoryList.stream().map(Category::getId).collect(Collectors.toSet());

          budgetDetailUpdateDTO.getBudgetDetailList().forEach(budgetDetailVO -> {
               isBudgetDetailValid(budgetDetailVO);

               if(!categorySet.contains(budgetDetailVO.getCatId()))
                    throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);

               budgetDetailList.add(BudgetDetail.builder()
                       .bgId(budgetDetailUpdateDTO.getBgId())
                       .catId(budgetDetailVO.getCatId())
                       .amount(budgetDetailVO.getAmount())
                       .build());
          });

          budgetDetailRepository.saveAll(budgetDetailList);
     }

     public void isUserAccessValid(Long accessUserId, Budget budget){
          if(budget.getDeleted())
               throw new CustomException(ErrorCode.BUDGET_DELETED);

          if(!budget.getUserId().equals(accessUserId))
               throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
     }

     public void isBudgetDetailValid(BudgetDetailVO budgetDetailVO){
          Long amount = budgetDetailVO.getAmount();
          Long catId = budgetDetailVO.getCatId();

          if(catId == null || amount == null || amount < 0)
               throw new CustomException(ErrorCode.INVALID_BUDGET_DETAIL);

     }

     public LocalDate monthPattern(LocalDate date){
          return LocalDate.of(date.getYear(), date.getMonth(), 1);
     }
}
