package wanted.n.budgetmanager.server.repository.q;

import wanted.n.budgetmanager.server.dto.SpendingBriefVO;
import wanted.n.budgetmanager.server.dto.SpendingListDTO;

import java.util.List;

public interface SpendingQRepository {

    List<SpendingBriefVO> getSpendingList(SpendingListDTO spendingListDTO);
}
