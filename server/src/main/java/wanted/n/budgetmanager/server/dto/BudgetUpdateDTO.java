package wanted.n.budgetmanager.server.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetUpdateDTO {
    private Long userId;
    private Long bgId;
    private List<BudgetDetailVO> budgetDetailList;

    public static BudgetUpdateDTO from(Long userId, BudgetUpdateRequestDTO budgetUpdateRequestDTO){

        return BudgetUpdateDTO.builder()
                .userId(userId)
                .budgetDetailList(budgetUpdateRequestDTO.getBudgetDetailList())
                .build();
    }
}
