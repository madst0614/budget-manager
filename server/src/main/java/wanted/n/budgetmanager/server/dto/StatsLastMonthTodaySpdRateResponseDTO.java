package wanted.n.budgetmanager.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsLastMonthTodaySpdRateResponseDTO {
    private Integer totalRate;
    private List<SpdCatRateVO> spdCatRateVOList;
}
