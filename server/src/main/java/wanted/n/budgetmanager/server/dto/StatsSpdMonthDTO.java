package wanted.n.budgetmanager.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsSpdMonthDTO {
    private Long userId;

    private LocalDate date;

    private List<Long> categoryList;

    public static StatsSpdMonthDTO from(StatsSpdMonthGetDTO statsSpdMonthGetDTO){
        return StatsSpdMonthDTO.builder()
                .userId(statsSpdMonthGetDTO.getUserId())
                .date(statsSpdMonthGetDTO.getDate())
                .categoryList(statsSpdMonthGetDTO.getCatIdList())
                .build();
    }
}
