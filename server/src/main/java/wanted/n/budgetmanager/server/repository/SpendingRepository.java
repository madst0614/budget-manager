package wanted.n.budgetmanager.server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import wanted.n.budgetmanager.server.domain.Spending;
import wanted.n.budgetmanager.server.repository.q.SpendingQRepository;

public interface SpendingRepository extends JpaRepository<Spending, Long>, SpendingQRepository {
}
