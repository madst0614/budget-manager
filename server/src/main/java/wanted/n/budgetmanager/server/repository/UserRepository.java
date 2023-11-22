package wanted.n.budgetmanager.server.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanted.n.budgetmanager.server.domain.User;

import java.util.Optional;

@Repository
@Primary
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
