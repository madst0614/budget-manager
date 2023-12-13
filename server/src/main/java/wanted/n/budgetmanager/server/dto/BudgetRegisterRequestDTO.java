package wanted.n.budgetmanager.server.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetRegisterRequestDTO {
    private List<BudgetDetailVO> budgetDetailVOList;
}
