package wanted.n.budgetmanager.server.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetRegisterRequestDTO {
    private LocalDateTime start;
    private LocalDateTime end;
}
