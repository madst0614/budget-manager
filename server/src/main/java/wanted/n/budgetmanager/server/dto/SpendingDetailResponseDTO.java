package wanted.n.budgetmanager.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.n.budgetmanager.server.domain.Spending;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpendingDetailResponseDTO {
    private Long id;

    private Long catId;

    private LocalDate date;

    private Integer amount;

    private String memo;

    private Boolean excluded;

    public static SpendingDetailResponseDTO from(Spending spending){
        return SpendingDetailResponseDTO.builder()
                .id(spending.getId())
                .catId(spending.getCatId())
                .date(spending.getDate())
                .amount(spending.getAmount())
                .memo(spending.getMemo())
                .excluded(spending.getExcluded())
                .build();
    }
}
