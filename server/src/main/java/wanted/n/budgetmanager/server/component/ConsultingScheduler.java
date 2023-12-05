package wanted.n.budgetmanager.server.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wanted.n.budgetmanager.server.domain.User;
import wanted.n.budgetmanager.server.service.ConsultingService;
import wanted.n.budgetmanager.server.service.UserService;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ConsultingScheduler {
    private final ConsultingService consultingService;
    private final UserService userService;

    // 매일 08시마다
    @Scheduled(cron = "0 0 8 * * ?")
    @Transactional
    public void sendTodaySpdNotice(){
        List<User> userList = userService.findAllUsers();

        for (User user : userList) {
            /**
               필요에 따라 알맞게 구현
             */
        }
    }
}
