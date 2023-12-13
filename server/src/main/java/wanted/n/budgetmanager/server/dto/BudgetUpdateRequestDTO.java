package wanted.n.budgetmanager.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetUpdateRequestDTO {
    private LocalDateTime date;

    List<BudgetDetailVO> budgetDetailList;
}
