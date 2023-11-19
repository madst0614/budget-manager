package wanted.n.budgetmanager.server.domain.pk;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class BudgetDetailPK implements Serializable {
    private Long bgId;
    private Long catId;
}
