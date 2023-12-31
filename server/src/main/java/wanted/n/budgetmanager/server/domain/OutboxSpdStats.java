package wanted.n.budgetmanager.server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.n.budgetmanager.server.enums.OutboxSpdStatsType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Table(name="outbox_spending_stats")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class OutboxSpdStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private OutboxSpdStatsType type;

    @Column
    @NotNull
    private Long userId;

    @Column
    @NotNull
    private Long catId;

    @Column
    @NotNull
    private LocalDate date;

    @Column
    @NotNull
    private Integer amount;

    public static OutboxSpdStats from(OutboxSpdStatsType type, Spending spending){
        return OutboxSpdStats.builder()
                .type(type)
                .userId(spending.getUserId())
                .catId(spending.getCatId())
                .date(spending.getDate())
                .amount(spending.getAmount())
                .build();
    }

    public static OutboxSpdStats from(OutboxSpdStatsType type, Integer amount, Spending spending){
        return OutboxSpdStats.builder()
                .type(type)
                .userId(spending.getUserId())
                .catId(spending.getCatId())
                .date(spending.getDate())
                .amount(amount)
                .build();
    }
}
