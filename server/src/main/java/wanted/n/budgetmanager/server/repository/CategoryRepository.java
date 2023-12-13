package wanted.n.budgetmanager.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanted.n.budgetmanager.server.domain.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByOrderByIdAsc();
}
