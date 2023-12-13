package wanted.n.budgetmanager.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpendingListDTO {
    private Long userId;

    private List<Long> categoryList;

    private LocalDate start;
    private LocalDate end;

    private Integer min;
    private Integer max;

}
