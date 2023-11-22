package wanted.n.budgetmanager.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.n.budgetmanager.server.domain.StatsSpdDay;
import wanted.n.budgetmanager.server.dto.StatsSpdDayDTO;
import wanted.n.budgetmanager.server.dto.StatsSpdDayGetDTO;
import wanted.n.budgetmanager.server.dto.StatsSpdDayUpdateDTO;
import wanted.n.budgetmanager.server.dto.StatsSpyDayResponseDTO;
import wanted.n.budgetmanager.server.exception.CustomException;
import wanted.n.budgetmanager.server.exception.ErrorCode;
import wanted.n.budgetmanager.server.repository.StatsSpdDayRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsService {
    private final StatsSpdDayRepository statsSpdDayRepository;

    @Transactional
    public StatsSpyDayResponseDTO getStatsSpdDay(StatsSpdDayGetDTO statsSpdDayGetDTO){
        StatsSpdDay statsSpdDay = statsSpdDayRepository
                .getSumByCatIdList(StatsSpdDayDTO.from(statsSpdDayGetDTO));

        validAccessCheck(statsSpdDay, statsSpdDayGetDTO.getUserId());

        return StatsSpyDayResponseDTO.builder()
                .id(statsSpdDay.getId())
                .userId(statsSpdDay.getUserId())
                .date(statsSpdDay.getDate())
                .catIdList(statsSpdDayGetDTO.getCatIdList())
                .sum(statsSpdDay.getSum())
                .build();
    }

    @Transactional
    public void updateStatsSpdDay(StatsSpdDayUpdateDTO statsSpdDayUpdateDTO) {
        StatsSpdDay statsSpdDay = statsSpdDayRepository.findStatsSpdDayByUserIdAndCatIdAndDate(
                statsSpdDayUpdateDTO.getUserId(), statsSpdDayUpdateDTO.getCatId(), statsSpdDayUpdateDTO.getDate());

        if(statsSpdDay==null){
            statsSpdDay = StatsSpdDay
                    .builder()
                    .userId(statsSpdDayUpdateDTO.getUserId())
                    .date(statsSpdDayUpdateDTO.getDate())
                    .catId(statsSpdDayUpdateDTO.getCatId())
                    .sum(0L)
                    .build();
        }

        validAccessCheck(statsSpdDay, statsSpdDayUpdateDTO.getUserId());

        statsSpdDayRepository.save(StatsSpdDay.builder()
                        .id(statsSpdDay.getId())
                        .userId(statsSpdDay.getUserId())
                        .date(statsSpdDay.getDate())
                        .catId(statsSpdDay.getCatId())
                        .sum(statsSpdDay.getSum()
                                + statsSpdDayUpdateDTO.getAmount())
                .build());
    }

    private static void validAccessCheck(StatsSpdDay statsSpdDay, Long userId){

        if(!statsSpdDay.getUserId().equals(userId))
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
    }
}
