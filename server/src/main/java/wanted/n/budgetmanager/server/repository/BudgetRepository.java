package wanted.n.budgetmanager.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.n.budgetmanager.server.domain.Budget;
import wanted.n.budgetmanager.server.repository.mapping.UserId;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<UserId> findUserIdById(Long id);

    List<Budget> findAllByUserIdAndDeleted(Long userId, Boolean deleted);
}
