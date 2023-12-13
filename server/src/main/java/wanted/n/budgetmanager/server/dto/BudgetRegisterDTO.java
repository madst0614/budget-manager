package wanted.n.budgetmanager.server.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetRegisterDTO {
    private Long userId;
    private LocalDate date;
    private List<BudgetDetailVO> budgetDetailVOList;
}
