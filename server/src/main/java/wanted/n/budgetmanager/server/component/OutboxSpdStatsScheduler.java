package wanted.n.budgetmanager.server.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wanted.n.budgetmanager.server.service.OutboxService;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OutboxSpdStatsScheduler {
    private final OutboxService outboxService;

    @Scheduled(cron = "0/10 * * * * ?")
    @Transactional
    public void consumeOutboxSpdStatsLimit100() {
        outboxService.consumeOutboxSpdStatsLimit100();
    }
}
