package wanted.n.budgetmanager.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.n.budgetmanager.server.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
