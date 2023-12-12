package wanted.n.budgetmanager.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanted.n.budgetmanager.server.domain.StatsBudgetDetailAverage;

import java.util.List;

@Repository
public interface StatsBudgetDetailAverageRepository extends JpaRepository<StatsBudgetDetailAverage, Long> {

    List<StatsBudgetDetailAverage> findAllByOrderByPercentage();
}
