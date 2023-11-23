package wanted.n.budgetmanager.server.repository.q;

import wanted.n.budgetmanager.server.domain.StatsSpdDay;
import wanted.n.budgetmanager.server.dto.StatsSpdDayDTO;

public interface StatsSpdDayQRepository {

    public StatsSpdDay getSumByCatIdList(StatsSpdDayDTO statsSpdDayDTO);

}
