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
public class TodaySpdNoticeResponseDTO {
    private Long recomTotal;

    private List<SpdCatAmountVO> recomSpdCatAmountVOList;

    private Long todayTotal;

    private List<SpdCatAmountVO> todaySpdCatAmountVOList;

    private List<TodaySpdRiskRateVO> todaySpdRiskRateVOList;

    public static TodaySpdNoticeResponseDTO from(TodaySpdRecomVO todaySpdRecomVO, TodaySpdInfoVO todaySpdInfoVO
            , List<TodaySpdRiskRateVO> todaySpdRiskRateVOList) {
        return TodaySpdNoticeResponseDTO.builder()
                .recomTotal(todaySpdRecomVO.getTotal())
                .recomSpdCatAmountVOList(todaySpdRecomVO.getSpdCatAmountVOList())
                .todayTotal(todaySpdInfoVO.getTodayTotal())
                .todaySpdCatAmountVOList(todaySpdInfoVO.getTodaySpdCatAmountVOList())
                .todaySpdRiskRateVOList(todaySpdRiskRateVOList)
                .build();
    }
}
