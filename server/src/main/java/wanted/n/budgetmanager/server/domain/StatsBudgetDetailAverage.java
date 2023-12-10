package wanted.n.budgetmanager.server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Table(name="stats_budget_detail_average")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class StatsBudgetDetailAverage extends BaseEntity{
    @Id
    private Long catId;

    private Integer percentage;
}
