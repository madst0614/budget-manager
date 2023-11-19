package wanted.n.budgetmanager.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.n.budgetmanager.server.domain.Budget;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetListResponseDTO {
    private List<Budget> budgetList;
}
