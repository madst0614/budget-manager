package wanted.n.budgetmanager.server.repository.q;

import wanted.n.budgetmanager.server.domain.StatsSpdMonth;
import wanted.n.budgetmanager.server.dto.StatsSpdMonthDTO;

public interface StatsSpdMonthQRepository {

    public StatsSpdMonth getSumByCatIdList(StatsSpdMonthDTO statsSpdMonthDTO);
}
