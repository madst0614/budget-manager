package wanted.n.budgetmanager.server.dto;

import lombok.Builder;
import lombok.Getter;
import wanted.n.budgetmanager.server.domain.BudgetDetail;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class BudgetDetailVO {
    private Long catId;
    private Long amount;

    public static BudgetDetailVO from(BudgetDetail budgetDetail){
        return BudgetDetailVO.builder()
                .catId(budgetDetail.getCatId())
                .amount(budgetDetail.getAmount())
                .build();
    }

    public static List<BudgetDetailVO> from(List<BudgetDetail> budgetDetailList){
        List<BudgetDetailVO> budgetDetailVOList = new ArrayList<>();

        budgetDetailList.forEach(budgetDetail -> {
            budgetDetailVOList.add(BudgetDetailVO.from(budgetDetail));
        });

        return budgetDetailVOList;
    }
}
