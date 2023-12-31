package wanted.n.budgetmanager.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanted.n.budgetmanager.server.domain.StatsSpdMonth;
import wanted.n.budgetmanager.server.repository.q.StatsSpdMonthQRepository;

import java.time.LocalDate;

@Repository
public interface StatsSpdMonthRepository extends JpaRepository<StatsSpdMonth, Long>, StatsSpdMonthQRepository {
    StatsSpdMonth findStatsSpdMonthByUserIdAndDateAndCatId(Long userId, LocalDate date,  Long catId);
}
