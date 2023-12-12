package wanted.n.budgetmanager.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.n.budgetmanager.server.domain.OutboxSpdStats;
import wanted.n.budgetmanager.server.domain.Spending;
import wanted.n.budgetmanager.server.dto.*;
import wanted.n.budgetmanager.server.enums.OutboxSpdStatsType;
import wanted.n.budgetmanager.server.exception.CustomException;
import wanted.n.budgetmanager.server.exception.ErrorCode;
import wanted.n.budgetmanager.server.repository.OutboxSpdStatsRepository;
import wanted.n.budgetmanager.server.repository.SpendingRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpendingService {
    private final SpendingRepository spendingRepository;
    private final OutboxSpdStatsRepository outboxSpdStatsRepository;
    private final CategoryService categoryService;

    @Transactional
    public SpendingListResponseDTO getSpendingList(SpendingListDTO spendingListDTO){
        List<SpendingBriefVO> spendingBriefVOList
                = spendingRepository.getSpendingList(spendingListDTO);

        // 전체 합계 및 카테고리별 합계 처리
        int sum = 0;
        Map<Long, Integer> catSum = new HashMap<>();

        for(SpendingBriefVO spendingBriefVO : spendingBriefVOList){
            if(!spendingBriefVO.getExcluded()){
                sum += spendingBriefVO.getAmount();

                catSum.put(spendingBriefVO.getCatId()
                        , catSum.getOrDefault(spendingBriefVO.getCatId(), 0)
                                + spendingBriefVO.getAmount());
            }

        }
        return SpendingListResponseDTO.builder()
                .sum(sum)
                .categoryList(spendingListDTO.getCategoryList())
                .catSum(catSum)
                .spendingBriefVOList(spendingBriefVOList)
                .build();
    }

    @Transactional
    public SpendingDetailResponseDTO getSpendingDetail(SpendingDetailRequestDTO spendingDetailRequestDTO){
        Spending spending = spendingRepository.findById(spendingDetailRequestDTO.getId())
                .orElseThrow(()->new CustomException(ErrorCode.SPENDING_NOT_FOUND));

        validAccessCheck(spending, spendingDetailRequestDTO.getUserId());

        return SpendingDetailResponseDTO.from(spending);
    }

    @Transactional
    public void createSpending(SpendingCreateDTO spendingCreateDTO){
        isCategoryValid(spendingCreateDTO.getCatId());

        Spending spending = spendingRepository.save(Spending.from(spendingCreateDTO));

        if(!spending.getExcluded()){
            outboxSpdStatsRepository.save(OutboxSpdStats.from(OutboxSpdStatsType.MONTH, spending));
            outboxSpdStatsRepository.save(OutboxSpdStats.from(OutboxSpdStatsType.DAY, spending));
        }

    }

    @Transactional
    public void updateSpending(SpendingUpdateDTO spendingUpdateDTO){
        Spending spending = spendingRepository.getReferenceById(spendingUpdateDTO.getId());

        validAccessCheck(spending, spendingUpdateDTO.getUserId());
        isCategoryValid(spendingUpdateDTO.getCatId());

        Spending modified = spendingRepository.save(Spending.from(spending.getDate(), spendingUpdateDTO));

        if(spending.getCatId() != modified.getCatId()){
            outboxSpdStatsRepository.save(
                    OutboxSpdStats.from(OutboxSpdStatsType.MONTH,
                            -spending.getAmount(), spending));
            outboxSpdStatsRepository.save(
                    OutboxSpdStats.from(OutboxSpdStatsType.DAY,
                            -spending.getAmount(), spending));

            outboxSpdStatsRepository.save(
                    OutboxSpdStats.from(OutboxSpdStatsType.MONTH,
                            modified));
            outboxSpdStatsRepository.save(
                    OutboxSpdStats.from(OutboxSpdStatsType.DAY,
                            modified));
        }
        else{
            int diff = modified.getAmount()-spending.getAmount();

            if(spendingUpdateDTO.getExcluded())
                diff = - spending.getAmount();

            if(diff != 0){
                outboxSpdStatsRepository.save(
                        OutboxSpdStats.from(OutboxSpdStatsType.MONTH,
                                diff, modified));
                outboxSpdStatsRepository.save(
                        OutboxSpdStats.from(OutboxSpdStatsType.DAY,
                                diff, modified));
            }
        }
    }

    @Transactional
    public void deleteSpending(SpendingDeleteDTO spendingDeleteDTO){
        Spending spending = spendingRepository.getReferenceById(spendingDeleteDTO.getId());

        validAccessCheck(spending, spendingDeleteDTO.getUserId());

        spendingRepository.save(Spending.builder()
                .id(spending.getId())
                .userId(spending.getUserId())
                .catId(spending.getCatId())
                .date(spending.getDate())
                .amount(spending.getAmount())
                .memo(spending.getMemo())
                .excluded(spending.getExcluded())
                .deleted(true)
                .build());

        outboxSpdStatsRepository.save(
                OutboxSpdStats.from(OutboxSpdStatsType.MONTH, -spending.getAmount(),spending));
        outboxSpdStatsRepository.save(
                OutboxSpdStats.from(OutboxSpdStatsType.DAY, -spending.getAmount(),spending));
    }

    private static void validAccessCheck(Spending spending, Long userId){

        if(!spending.getUserId().equals(userId))
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);


        if(spending.getDeleted())
            throw new CustomException(ErrorCode.SPENDING_DELETED);

    }

    public void isCategoryValid(Long id){

        categoryService.getCategory(id)
                .orElseThrow(()->new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

    }

}
