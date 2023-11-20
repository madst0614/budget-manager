package wanted.n.budgetmanager.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpendingUpdateDTO {
    private Long userId;

    private Long id;

    private Long catId;

    @NotNull(message = "값을 입력하세요.")
    private LocalDate date;

    @NotNull(message = "값을 입력하세요.")
    private Integer amount;

    private String memo;

    @NotNull(message = "값을 입력하세요.")
    private Boolean excluded;
}
