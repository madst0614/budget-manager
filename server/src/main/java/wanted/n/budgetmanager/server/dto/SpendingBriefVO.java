package wanted.n.budgetmanager.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpendingBriefVO {
    private Long id;

    private Long userId;

    private Long catId;

    private LocalDate date;

    private Integer amount;

    private Boolean excluded;
}
