package wanted.n.budgetmanager.server.domain;

import lombok.*;
import wanted.n.budgetmanager.server.dto.SpendingCreateDTO;
import wanted.n.budgetmanager.server.dto.SpendingUpdateDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Spending {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long catId;

    @Column
    @NotNull(message = "값을 입력하세요.")
    private LocalDate date;

    @Column
    @NotNull(message = "값을 입력하세요.")
    private Integer amount;

    @Column
    private String memo;

    @Column
    @NotNull
    private Boolean excluded;

    @Column
    @NotNull
    private Boolean deleted;

    public static Spending from(SpendingCreateDTO spendingCreateDTO){
        return Spending.builder()
                .userId(spendingCreateDTO.getUserId())
                .catId(spendingCreateDTO.getCatId())
                .date(spendingCreateDTO.getDate())
                .amount(spendingCreateDTO.getAmount())
                .memo(spendingCreateDTO.getMemo())
                .excluded(spendingCreateDTO.getExcluded())
                .deleted(false)
                .build();
    }

    public static Spending from(SpendingUpdateDTO spendingUpdateDTO){
        return Spending.builder()
                .id(spendingUpdateDTO.getId())
                .userId(spendingUpdateDTO.getUserId())
                .catId(spendingUpdateDTO.getCatId())
                .date(spendingUpdateDTO.getDate())
                .amount(spendingUpdateDTO.getAmount())
                .memo(spendingUpdateDTO.getMemo())
                .excluded(spendingUpdateDTO.getExcluded())
                .deleted(false)
                .build();
    }
}
