package wanted.n.budgetmanager.server.repository.q;

import wanted.n.budgetmanager.server.domain.SpdCatAmountVO;

import java.util.List;

public interface BudgetDetailQRepository {
    List<SpdCatAmountVO> getSpdCatAmountVOListByUserIdOrderByCatId(Long userId);

}
