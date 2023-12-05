package wanted.n.budgetmanager.server.repository.q;

import wanted.n.budgetmanager.server.domain.SpdCatAmountVO;
import wanted.n.budgetmanager.server.domain.StatsSpdDay;
import wanted.n.budgetmanager.server.dto.StatsSpdDayDTO;

import java.time.LocalDate;
import java.util.List;

public interface StatsSpdDayQRepository {

    public StatsSpdDay getSumByCatIdList(StatsSpdDayDTO statsSpdDayDTO);

    public List<SpdCatAmountVO> findSumListByDateAndUserIdOrderByCatId(LocalDate date, Long userId);
}
