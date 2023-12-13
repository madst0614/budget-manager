package wanted.n.budgetmanager.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wanted.n.budgetmanager.server.domain.OutboxSpdStats;
import wanted.n.budgetmanager.server.domain.StatsSpdDay;
import wanted.n.budgetmanager.server.domain.StatsSpdMonth;
import wanted.n.budgetmanager.server.enums.OutboxSpdStatsType;
import wanted.n.budgetmanager.server.repository.OutboxSpdStatsRepository;
import wanted.n.budgetmanager.server.repository.StatsSpdDayRepository;
import wanted.n.budgetmanager.server.repository.StatsSpdMonthRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxSpdStatsRepository outboxSpdStatsRepository;
    private final StatsSpdMonthRepository statsSpdMonthRepository;
    private final StatsSpdDayRepository statsSpdDayRepository;

    @Transactional
    public void consumeOutboxSpdStatsLimit100() {
        List<OutboxSpdStats> outboxSpdStatsList = outboxSpdStatsRepository.findTop100By();

        for (OutboxSpdStats outboxSpdStats : outboxSpdStatsList) {
            if(outboxSpdStats.getType() == OutboxSpdStatsType.MONTH){
                StatsSpdMonth statsSpdMonth =
                        statsSpdMonthRepository.findStatsSpdMonthByUserIdAndDateAndCatId(
                                outboxSpdStats.getUserId(), monthPattern(outboxSpdStats.getDate()), outboxSpdStats.getCatId());

                // 데이터가 없으면 Default 설정
                if(statsSpdMonth == null){
                    statsSpdMonth = StatsSpdMonth.getDefault(outboxSpdStats);
                }

                statsSpdMonth.updateSum(outboxSpdStats.getAmount());

                statsSpdMonthRepository.save(statsSpdMonth);
            }else if(outboxSpdStats.getType() == OutboxSpdStatsType.DAY){
                StatsSpdDay statsSpdDay
                        = statsSpdDayRepository.findStatsSpdDayByUserIdAndDateAndCatId(
                        outboxSpdStats.getUserId(), outboxSpdStats.getDate(), outboxSpdStats.getCatId());

                // 데이터가 없으면 Default 설정
                if(statsSpdDay == null){
                    statsSpdDay = StatsSpdDay.getDefault(outboxSpdStats);
                }

                statsSpdDay.updateSum(outboxSpdStats.getAmount());

                statsSpdDayRepository.save(statsSpdDay);
            }
        }

        // 필요시 로그 보존
        outboxSpdStatsRepository.deleteAll(outboxSpdStatsList);
    }

    private static LocalDate monthPattern(LocalDate localDate){
        return LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
    }
}
