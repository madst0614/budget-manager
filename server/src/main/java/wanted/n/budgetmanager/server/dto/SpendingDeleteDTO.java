package wanted.n.budgetmanager.server.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpendingDeleteDTO {
    private Long userId;
    private Long id;
}
