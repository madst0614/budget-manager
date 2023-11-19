package wanted.n.budgetmanager.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.n.budgetmanager.server.domain.BudgetDetail;
import wanted.n.budgetmanager.server.domain.pk.BudgetDetailPK;

import java.util.List;

public interface BudgetDetailRepository extends JpaRepository<BudgetDetail, BudgetDetailPK> {
    List<BudgetDetail> findAllByBgId(Long bgId);
}
