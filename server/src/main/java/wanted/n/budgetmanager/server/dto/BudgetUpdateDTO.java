package wanted.n.budgetmanager.server.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetUpdateDTO {
    private Long userId;
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;

    public static BudgetUpdateDTO from(Long userId, BudgetUpdateRequestDTO budgetUpdateRequestDTO){

        return BudgetUpdateDTO.builder()
                .userId(userId)
                .start(budgetUpdateRequestDTO.getStart())
                .end(budgetUpdateRequestDTO.getEnd())
                .build();
    }
}
