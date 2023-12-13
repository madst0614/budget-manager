package wanted.n.budgetmanager.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpendingListResponseDTO {
    private Integer sum;
    private List<Long> categoryList;
    private Map<Long, Integer> catSum;
    private List<SpendingBriefVO> spendingBriefVOList;
}
