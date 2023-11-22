package wanted.n.budgetmanager.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.n.budgetmanager.server.domain.StatsSpdDay;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsSpyDayResponseDTO {
    private Long id;

    private Long userId;

    private LocalDate date;

    private List<Long> catIdList;

    private Long sum;

    public static StatsSpyDayResponseDTO from(StatsSpdDay statsSpdDay) {
        return StatsSpyDayResponseDTO.builder()
                .id(statsSpdDay.getId())
                .userId(statsSpdDay.getUserId())
                .date(statsSpdDay.getDate())
                .sum(statsSpdDay.getSum())
                .build();
    }
}
