package wanted.n.budgetmanager.server.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wanted.n.budgetmanager.server.domain.OutboxSpdStats;
import wanted.n.budgetmanager.server.domain.StatsSpdDay;
import wanted.n.budgetmanager.server.domain.StatsSpdMonth;
import wanted.n.budgetmanager.server.enums.OutboxSpdStatsType;
import wanted.n.budgetmanager.server.repository.OutboxSpdStatsRepository;
import wanted.n.budgetmanager.server.repository.StatsSpdDayRepository;
import wanted.n.budgetmanager.server.repository.StatsSpdMonthRepository;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class OutboxSpdStatsScheduler {
    private final OutboxSpdStatsRepository outboxSpdStatsRepository;
    private final StatsSpdMonthRepository statsSpdMonthRepository;
    private final StatsSpdDayRepository statsSpdDayRepository;

    @Scheduled(cron = "0/10 * * * * ?")
    @Transactional
    public void consumeOutboxSpdStats() {
        log.info("통계 갱신");
        List<OutboxSpdStats> outboxSpdStatsList = outboxSpdStatsRepository.findTop100By();

        for (OutboxSpdStats outboxSpdStats : outboxSpdStatsList) {
            if(outboxSpdStats.getType() == OutboxSpdStatsType.MONTH){
                StatsSpdMonth statsSpdMonth =
                        statsSpdMonthRepository.findStatsSpdMonthByUserIdAndDateAndCatId(
                                outboxSpdStats.getUserId(), outboxSpdStats.getDate(), outboxSpdStats.getCatId());

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
}
