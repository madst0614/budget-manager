package wanted.n.budgetmanager.server.repository.q;

import wanted.n.budgetmanager.server.domain.SpdCatAmountVO;
import wanted.n.budgetmanager.server.domain.StatsSpdDay;
import wanted.n.budgetmanager.server.dto.StatsSpdDayDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StatsSpdDayQRepository {

    public StatsSpdDay findSumByCatIdList(StatsSpdDayDTO statsSpdDayDTO);

    public List<SpdCatAmountVO> findSumListByDateAndUserIdOrderByCatId(LocalDate date, Long userId);

    public List<SpdCatAmountVO> findSumListByPeriodAndUserIdOrderByCatId(LocalDate start, LocalDate end, Long userId);

    Optional<Long> findAllSumByDateAndUserId(LocalDate date, Long userId);
}
