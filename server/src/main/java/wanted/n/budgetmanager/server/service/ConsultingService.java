package wanted.n.budgetmanager.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.n.budgetmanager.server.domain.SpdCatAmountVO;
import wanted.n.budgetmanager.server.dto.*;
import wanted.n.budgetmanager.server.repository.BudgetDetailRepository;
import wanted.n.budgetmanager.server.repository.StatsSpdDayRepository;
import wanted.n.budgetmanager.server.repository.StatsSpdMonthRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsultingService {
    private final StatsSpdDayRepository statsSpdDayRepository;
    private final StatsSpdMonthRepository statsSpdMonthRepository;
    private final BudgetDetailRepository budgetDetailRepository;

    @Transactional
    public TodaySpdRecomResponseDTO todaySpendingRecommend (TodaySpdRecomRequestDTO todaySpdRecomRequestDTO){
        Long userId = todaySpdRecomRequestDTO.getUserId();
        LocalDate now = todaySpdRecomRequestDTO.getNow();

        // MonthRepo 양식으로 변경 'yyyy-MM-1';
        LocalDate month = dayPatternToMonthPattern(now);

        List<SpdCatAmountVO> spdCatDayList = statsSpdDayRepository.findSumListByDateAndUserIdOrderByCatId(now, userId);

        List<SpdCatAmountVO> spdCatMonthList = statsSpdMonthRepository.findSumListByDateAndUserIdOrderByCatid(month, userId);

        List<SpdCatAmountVO> currentSpdCatAmountVOList = calcCurrentCatAmount(spdCatDayList, spdCatMonthList);

        List<SpdCatAmountVO> budgetSpdCatAmountVOList = budgetDetailRepository.getSpdCatAmountVOListByUserIdOrderByCatId(
                userId);

        return TodaySpdRecomResponseDTO.from(todaySpdRecomLogic(
                currentSpdCatAmountVOList
                , budgetSpdCatAmountVOList));
    }

    @Transactional
    public TodaySpdNoticeResponseDTO todaySpendingNotice(TodaySpdNoticeRequestDTO todaySpdNoticeRequestDTO){
        Long userId = todaySpdNoticeRequestDTO.getUserId();
        LocalDate now = todaySpdNoticeRequestDTO.getNow();
        LocalDate month = dayPatternToMonthPattern(now);

        List<SpdCatAmountVO> spdCatDayList = statsSpdDayRepository.findSumListByDateAndUserIdOrderByCatId(now, userId);

        List<SpdCatAmountVO> spdCatMonthList = statsSpdMonthRepository.findSumListByDateAndUserIdOrderByCatid(month, userId);

        List<SpdCatAmountVO> currentSpdCatAmountVOList = calcCurrentCatAmount(spdCatDayList, spdCatMonthList);

        List<SpdCatAmountVO> budgetSpdCatAmountVOList = budgetDetailRepository.getSpdCatAmountVOListByUserIdOrderByCatId(userId);

        TodaySpdRecomVO todaySpdRecomVO = todaySpdRecomLogic(currentSpdCatAmountVOList, budgetSpdCatAmountVOList);

        List<TodaySpdRiskRateVO> todaySpdRiskRateVOList = calcTodaySpdRiskRate(spdCatDayList, todaySpdRecomVO.getSpdCatAmountVOList());

        return TodaySpdNoticeResponseDTO.from(todaySpdRecomVO, calcTodaySpdInfo(spdCatDayList), todaySpdRiskRateVOList) ;
    }

    private static TodaySpdRecomVO todaySpdRecomLogic(List<SpdCatAmountVO> current, List<SpdCatAmountVO> budget){
        LocalDate now  = LocalDate.now();
        double remainRate = (double)1 / (double) (now.lengthOfMonth() - now.getDayOfMonth() + 1); // 남은 기간 비율
        double dayRate = (double) 1/ (double) now.lengthOfMonth(); // 한달 비율
        double minRate = 0.7; // 최소 비율

        List<SpdCatAmountVO> recommendList = new ArrayList<>();
        long total = 0L;

        for(int i=0; i<budget.size(); i++){
            long catId = budget.get(i).getCatId();
            long amount = budget.get(i).getAmount(); // 추천 예산(init = 전체 예산)

            if(current.get(i).getAmount().equals(0L)){  // 만약 기간 중에 사용한 금액이 없다면
                amount = (long) (amount * remainRate);
            }else{
                if(amount > current.get(i).getAmount()){ // 만약 예산 초과 X
                    amount -= current.get(i).getAmount();
                    amount = (long) (amount * remainRate);
                }
                else{ // 예산 초과 했다면 => 원래 하루당 예산 * minRate
                    amount = (long)(amount * dayRate * minRate);
                }

            }

            amount = (amount/10)*10;

            total += amount;

            recommendList.add(SpdCatAmountVO.builder()
                    .catId(catId)
                    .amount(amount)
                    .build());
        }

        return TodaySpdRecomVO
                .builder()
                .total(total)
                .spdCatAmountVOList(recommendList)
                .build();
    }

    private List<TodaySpdRiskRateVO> calcTodaySpdRiskRate(List<SpdCatAmountVO> current,
                                                          List<SpdCatAmountVO> recommend
            ){
        List<TodaySpdRiskRateVO> todaySpdRiskRateVOList = new ArrayList<>();

        for(int i=0; i<recommend.size(); i++){

            todaySpdRiskRateVOList.add(TodaySpdRiskRateVO.builder()
                    .catId(recommend.get(i).getCatId())
                            .riskRate((int)(100 * current.get(i).getAmount() / recommend.get(i).getAmount()))
                    .build());
        }

        return todaySpdRiskRateVOList;
    }


    private static TodaySpdInfoVO calcTodaySpdInfo(List<SpdCatAmountVO> spdCatDayList){
        long total = 0;

        for(SpdCatAmountVO spdCatAmountVO: spdCatDayList){
            total += spdCatAmountVO.getAmount();
        }

        return TodaySpdInfoVO.builder()
                .todayTotal(total)
                .todaySpdCatAmountVOList(spdCatDayList)
                .build();
    }




    private LocalDate dayPatternToMonthPattern(LocalDate localDate){
        return LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
    }

    private List<SpdCatAmountVO> calcCurrentCatAmount(List<SpdCatAmountVO> spdDayList
            , List<SpdCatAmountVO> spdMonthList){

        List<SpdCatAmountVO> spdCatAmountVOList = new ArrayList<>();

        for(int i=0; i<spdDayList.size(); i++){
            spdCatAmountVOList.add(SpdCatAmountVO.builder()
                            .catId(spdDayList.get(i).getCatId())
                            .amount(spdMonthList.get(i).getAmount()-spdDayList.get(i).getAmount())
                    .build());
        }

        return spdCatAmountVOList;
    }
}
