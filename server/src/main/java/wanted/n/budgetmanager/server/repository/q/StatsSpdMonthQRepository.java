package wanted.n.budgetmanager.server.repository.q;

import wanted.n.budgetmanager.server.domain.SpdCatAmountVO;
import wanted.n.budgetmanager.server.domain.StatsSpdMonth;
import wanted.n.budgetmanager.server.dto.StatsSpdMonthDTO;

import java.time.LocalDate;
import java.util.List;

public interface StatsSpdMonthQRepository {

    public StatsSpdMonth getSumByCatIdList(StatsSpdMonthDTO statsSpdMonthDTO);

    List<SpdCatAmountVO> findSumListByDateAndUserIdOrderByCatid(LocalDate date, Long userId);
}
