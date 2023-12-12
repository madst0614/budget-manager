package wanted.n.budgetmanager.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.n.budgetmanager.server.domain.SpdCatAmountVO;
import wanted.n.budgetmanager.server.domain.StatsBudgetDetailAverage;
import wanted.n.budgetmanager.server.domain.StatsSpdDay;
import wanted.n.budgetmanager.server.dto.*;
import wanted.n.budgetmanager.server.exception.CustomException;
import wanted.n.budgetmanager.server.exception.ErrorCode;
import wanted.n.budgetmanager.server.repository.BudgetDetailRepository;
import wanted.n.budgetmanager.server.repository.StatsBudgetDetailAverageRepository;
import wanted.n.budgetmanager.server.repository.StatsSpdDayRepository;
import wanted.n.budgetmanager.server.repository.StatsSpdMonthRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsService {
    private final StatsSpdDayRepository statsSpdDayRepository;
    private final StatsSpdMonthRepository statsSpdMonthRepository;
    private final StatsBudgetDetailAverageRepository statsBudgetDetailAverageRepository;
    private final BudgetDetailRepository budgetDetailRepository;
    private final RedisService redisService;

    @Transactional
    public StatsBudgetDetailAverageResponseDTO getStatsBudgetDetailAverage() {
        List<StatsBudgetDetailAverage> statsBudgetDetailAverageList = statsBudgetDetailAverageRepository.findAllByOrderByPercentage();

        if(statsBudgetDetailAverageList.isEmpty()){
            /*
                평균 갱신 이전 요청 시 처리
             */
        }

        return StatsBudgetDetailAverageResponseDTO.builder().statsBudgetDetailAverageList(statsBudgetDetailAverageList).build();
    }

    @Transactional
    public void updateStatsBudgetDetailAverage() {
        List<SpdCatAmountVO> allSumEachCatId = budgetDetailRepository.getAllSumGroupByCatIdOrderBySum();
        List<StatsBudgetDetailAverage> statsBudgetDetailAverageList = new ArrayList<>();

        long totalAmount = 0L;
        int totalPercent = 100;

        for(SpdCatAmountVO spdCatAmountVO : allSumEachCatId){
            totalAmount += spdCatAmountVO.getAmount();
        }

        if(totalAmount == 0)
            throw new CustomException(ErrorCode.NO_ENOUGH_DATA);

        for(int i=0; i< allSumEachCatId.size(); i++){
            int percent = 0;

            if(i == allSumEachCatId.size()-1){
                percent = totalPercent;
            }
            else{
                percent = (int) ((allSumEachCatId.get(i).getAmount() * 100)/ totalAmount);
                totalPercent -= percent;
            }

            statsBudgetDetailAverageList.add(StatsBudgetDetailAverage
                    .builder()
                            .catId(allSumEachCatId.get(i).getCatId())
                            .percentage(percent)
                    .build());
        }

        statsBudgetDetailAverageRepository.saveAll(statsBudgetDetailAverageList);

    }

    @Transactional
    public StatsLastMonthTodaySpdRateResponseDTO getStatsLastMonthTodaySpdRate(
            StatsLastMonthTodaySpdRateRequestDTO statsLastMonthTodaySpdRateRequestDTO){
        Long userId = statsLastMonthTodaySpdRateRequestDTO.getUserId();
        LocalDate now = statsLastMonthTodaySpdRateRequestDTO.getNow();
        LocalDate nowMonthPattern = LocalDate.of(now.getYear(), now.getMonth(), 1);

        LocalDate lastMonthToday = now.minusMonths(1);
        LocalDate lastMonthStart = LocalDate.of(lastMonthToday.getYear(), lastMonthToday.getMonth(), 1);

        List<SpdCatAmountVO> lastMonthSpdList = statsSpdDayRepository.
                findSumListByPeriodAndUserIdOrderByCatId(lastMonthStart, lastMonthToday, userId);

        List<SpdCatAmountVO> thisMonthSpdList = statsSpdMonthRepository.
                findSumListByDateAndUserIdOrderByCatId(nowMonthPattern, userId);

        long lastTotal = 0L;
        long thisTotal = 0L;
        int totalRate = 0;
        List<SpdCatRateVO> spdCatRateVOList = new ArrayList<>();

        for(int i=0; i<lastMonthSpdList.size(); i++){
            int rate = 0;

            if(lastMonthSpdList.get(i).getAmount() > 0){
                rate = (int)(thisMonthSpdList.get(i).getAmount()*100L / lastMonthSpdList.get(i).getAmount());
            }

            lastTotal += lastMonthSpdList.get(i).getAmount();
            thisTotal += thisMonthSpdList.get(i).getAmount();

            spdCatRateVOList.add(SpdCatRateVO.builder()
                    .catId(lastMonthSpdList.get(i).getCatId())
                    .rate(rate)
                    .build());
        }

        if(lastTotal > 0)
            totalRate = (int)(thisTotal * 100L / lastTotal);

        return StatsLastMonthTodaySpdRateResponseDTO.builder()
                .totalRate(totalRate)
                .spdCatRateVOList(spdCatRateVOList).build();
    }

    @Transactional
    public StatsLastWeekTodaySpdRateResponseDTO getStatsLastWeekTodaySpdRate(
            StatsLastWeekTodaySpdRateRequestDTO statsLastWeekTodaySpdRateRequestDTO){
        Long userId = statsLastWeekTodaySpdRateRequestDTO.getUserId();
        LocalDate now = statsLastWeekTodaySpdRateRequestDTO.getNow();

        long lastWeek = statsSpdDayRepository.findAllSumByDateAndUserId(
                now.minusWeeks(1), userId).orElse(0L);
        long today = statsSpdDayRepository.findAllSumByDateAndUserId(now, userId).orElse(0L);


        int rate = 0;

        if(lastWeek > 0)
            rate = (int)(today * 100 / lastWeek);

        return StatsLastWeekTodaySpdRateResponseDTO.builder().rate(rate).build();
    }

    @Transactional
    public StatsOthersMeSpdRateResponseDTO getStatsOthersMeSpdRate(
            StatsOthersMeSpdRateRequestDTO statsOthersMeSpdRateRequestDTO){
        Long userId = statsOthersMeSpdRateRequestDTO.getUserId();
        LocalDate now = statsOthersMeSpdRateRequestDTO.getNow();
        LocalDate nowMonthPattern = LocalDate.of(now.getYear(), now.getMonth(), 1);


        String str = redisService.getBudgetUsersRate();
        int budgetUsersRate = 0;

        if(str==null){
            Long allBudgetSum = budgetDetailRepository.getAllSum().orElse(0L);
            Long allUserSum = statsSpdMonthRepository.findAllSumByDate(nowMonthPattern).orElse(0L);

            if(allBudgetSum > 0L)
                budgetUsersRate = (int)(allUserSum * 100 / allBudgetSum);

            redisService.saveBudgetUsersRate(budgetUsersRate);
        }
        else{
            budgetUsersRate = Integer.parseInt(str);
        }

        int myRate = 0;
        Long myBudgetSum = budgetDetailRepository.getAllSumByUserId(userId).orElse(0L);
        Long mySpdSum = statsSpdMonthRepository.findAllSumByDateAndUserId(nowMonthPattern, userId).orElse(0L);

        if(myBudgetSum > 0L)
          myRate = (int) (mySpdSum * 100L / myBudgetSum);

        int rate = 0;

        if(budgetUsersRate > 0)
            rate = myRate * 100 / budgetUsersRate;

        return StatsOthersMeSpdRateResponseDTO.builder().rate(rate).build();
    }



    private static void validAccessCheck(StatsSpdDay statsSpdDay, Long userId){

        if(!statsSpdDay.getUserId().equals(userId))
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
    }
}
