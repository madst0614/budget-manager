package wanted.n.budgetmanager.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanted.n.budgetmanager.server.domain.StatsSpdDay;
import wanted.n.budgetmanager.server.repository.q.StatsSpdDayQRepository;

import java.time.LocalDate;

@Repository
public interface StatsSpdDayRepository extends JpaRepository<StatsSpdDay, Long>, StatsSpdDayQRepository {
    StatsSpdDay findStatsSpdDayByUserIdAndDateAndCatId(Long userId, LocalDate date , Long catId);

}
