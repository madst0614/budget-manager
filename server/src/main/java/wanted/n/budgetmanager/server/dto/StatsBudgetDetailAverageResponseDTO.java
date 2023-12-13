package wanted.n.budgetmanager.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.n.budgetmanager.server.domain.StatsBudgetDetailAverage;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsBudgetDetailAverageResponseDTO {
    List<StatsBudgetDetailAverage> statsBudgetDetailAverageList;
}
