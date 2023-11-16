package wanted.n.budgetmanager.server.dto;

import lombok.*;
import wanted.n.budgetmanager.server.domain.BudgetDetail;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetDetailUpdateDTO {
    private Long userId;
    private Long bgId;
    private List<BudgetDetail> budgetDetailList;

    public static BudgetDetailUpdateDTO from(Long userId, BudgetDetailUpdateRequestDTO budgetDetailUpdateRequestDTO){

        return BudgetDetailUpdateDTO.builder()
                .userId(userId)
                .budgetDetailList(budgetDetailUpdateRequestDTO.getBudgetDetailList())
                .build();
    }
}
