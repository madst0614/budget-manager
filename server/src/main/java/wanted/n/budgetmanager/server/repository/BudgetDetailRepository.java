package wanted.n.budgetmanager.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanted.n.budgetmanager.server.domain.Budget;
import wanted.n.budgetmanager.server.domain.BudgetDetail;
import wanted.n.budgetmanager.server.domain.pk.BudgetDetailPK;
import wanted.n.budgetmanager.server.repository.q.BudgetDetailQRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetDetailRepository extends JpaRepository<BudgetDetail, BudgetDetailPK>, BudgetDetailQRepository {
    List<BudgetDetail> findAllByBgId(Long bgId);
}
