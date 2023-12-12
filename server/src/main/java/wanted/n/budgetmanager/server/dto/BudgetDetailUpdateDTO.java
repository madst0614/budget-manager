package wanted.n.budgetmanager.server.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetDetailUpdateDTO {
    private Long userId;
    private Long bgId;
    private List<BudgetDetailVO> budgetDetailList;

    public static BudgetDetailUpdateDTO from(Long userId, BudgetDetailUpdateRequestDTO budgetDetailUpdateRequestDTO){

        return BudgetDetailUpdateDTO.builder()
                .userId(userId)
                .budgetDetailList(budgetDetailUpdateRequestDTO.getBudgetDetailList())
                .build();
    }
}
