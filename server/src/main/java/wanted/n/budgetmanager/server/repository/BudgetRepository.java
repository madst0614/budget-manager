package wanted.n.budgetmanager.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanted.n.budgetmanager.server.domain.Budget;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findAllByUserIdAndDeleted(Long userId, Boolean deleted);

    Optional<Budget> findByUserIdAndDateAndDeleted(Long userId, LocalDate now, Boolean deleted);

    Optional<Budget> findByUserIdAndDate(Long userId, LocalDate month);
}
