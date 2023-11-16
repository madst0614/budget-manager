package wanted.n.budgetmanager.server.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetRegisterDTO {
    private Long userId;
    private LocalDateTime start;
    private LocalDateTime end;

    public static BudgetRegisterDTO from(Long userId, BudgetRegisterRequestDTO budgetRegisterRequestDTO){
        return BudgetRegisterDTO.builder()
                .userId(userId)
                .start(budgetRegisterRequestDTO.getStart())
                .end(budgetRegisterRequestDTO.getEnd())
                .build();
    }
}
