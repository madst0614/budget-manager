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
public class StatsSpdDayDTO {
    private Long userId;

    private LocalDate date;

    private List<Long> categoryList;

    public static StatsSpdDayDTO from(StatsSpdDayGetDTO statsSpdDayGetDTO){
        return StatsSpdDayDTO.builder()
                .userId(statsSpdDayGetDTO.getUserId())
                .date(statsSpdDayGetDTO.getDate())
                .categoryList(statsSpdDayGetDTO.getCatIdList())
                .build();
    }
}
