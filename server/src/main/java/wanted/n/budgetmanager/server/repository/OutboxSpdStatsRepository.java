package wanted.n.budgetmanager.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanted.n.budgetmanager.server.domain.OutboxSpdStats;

import java.util.List;

@Repository
public interface OutboxSpdStatsRepository extends JpaRepository<OutboxSpdStats, Long> {
    List<OutboxSpdStats> findTop100By();
}
