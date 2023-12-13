package wanted.n.budgetmanager.server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.n.budgetmanager.server.domain.pk.BudgetDetailPK;
import wanted.n.budgetmanager.server.dto.BudgetDetailVO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

    public static List<BudgetDetail> from(Long bgId, List<BudgetDetailVO> budgetDetailVOList) {
        List<BudgetDetail> budgetDetailList = new ArrayList<>();

        for(BudgetDetailVO budgetDetailVO : budgetDetailVOList){
            budgetDetailList.add(BudgetDetail.builder()
                    .bgId(bgId)
                    .catId(budgetDetailVO.getCatId())
                    .amount(budgetDetailVO.getAmount())
                    .build());
        }


        return budgetDetailList;
    }
}
