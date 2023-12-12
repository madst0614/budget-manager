package wanted.n.budgetmanager.server.repository.q;

import wanted.n.budgetmanager.server.domain.SpdCatAmountVO;
import wanted.n.budgetmanager.server.domain.StatsSpdMonth;
import wanted.n.budgetmanager.server.dto.StatsSpdMonthDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StatsSpdMonthQRepository {

    public StatsSpdMonth getSumByCatIdList(StatsSpdMonthDTO statsSpdMonthDTO);

    List<SpdCatAmountVO> findSumListByDateAndUserIdOrderByCatId(LocalDate date, Long userId);

    Optional<Long> findAllSumByDateAndUserId(LocalDate date, Long userId);

    Optional<Long> findAllSumByDate(LocalDate date);
}
