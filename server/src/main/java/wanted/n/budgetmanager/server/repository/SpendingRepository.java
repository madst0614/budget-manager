package wanted.n.budgetmanager.server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanted.n.budgetmanager.server.domain.Spending;
import wanted.n.budgetmanager.server.repository.q.SpendingQRepository;

@Repository
public interface SpendingRepository extends JpaRepository<Spending, Long>, SpendingQRepository {
}
