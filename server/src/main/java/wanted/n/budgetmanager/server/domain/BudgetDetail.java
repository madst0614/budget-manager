package wanted.n.budgetmanager.server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.n.budgetmanager.server.domain.pk.BudgetDetailPK;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@IdClass(BudgetDetailPK.class)
@Table(name="budget_detail")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class BudgetDetail {
    @Id
    private Long bgId;

    @Id
    private Long catId;

    @Column
    @NotNull(message = "값을 입력하세요.")
    private Long amount;
}
