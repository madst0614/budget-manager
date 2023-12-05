package wanted.n.budgetmanager.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.n.budgetmanager.server.domain.SpdCatAmountVO;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodaySpdRecomResponseDTO {
    private Long total;

    private List<SpdCatAmountVO> spdCatAmountVOList;

    public static TodaySpdRecomResponseDTO from(TodaySpdRecomVO todaySpdRecomVO){
        return TodaySpdRecomResponseDTO.builder()
                .total(todaySpdRecomVO.getTotal())
                .spdCatAmountVOList(todaySpdRecomVO.getSpdCatAmountVOList())
                .build();
    }
}
