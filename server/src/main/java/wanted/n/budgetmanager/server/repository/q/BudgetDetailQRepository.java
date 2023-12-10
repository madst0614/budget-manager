package wanted.n.budgetmanager.server.repository.q;

import wanted.n.budgetmanager.server.domain.SpdCatAmountVO;

import java.util.List;
import java.util.Optional;

public interface BudgetDetailQRepository {

    List<SpdCatAmountVO> getSpdCatAmountVOListByUserIdOrderByCatId(Long userId);

    List<SpdCatAmountVO> getAllSumGroupByCatIdOrderBySum();

    Optional<Long> getAllSum();

    Optional<Long> getAllSumByUserId(Long userId);
}
