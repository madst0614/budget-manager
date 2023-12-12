package wanted.n.budgetmanager.server.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wanted.n.budgetmanager.server.service.StatsService;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StatsScheduler {
    private final StatsService statsService;

//    @Scheduled(cron = "0 0 */1 * * ?")
@Scheduled(cron = "0/10 * * * * ?")
    @Transactional
    public void updateStatsBudgetDetailAverage() {
        statsService.updateStatsBudgetDetailAverage();
    }
}
