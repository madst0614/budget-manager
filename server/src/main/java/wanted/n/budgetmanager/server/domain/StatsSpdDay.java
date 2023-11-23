package wanted.n.budgetmanager.server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Table(name="stats_spd_day")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class StatsSpdDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull(message = "값을 입력하세요.")
    private Long userId;

    @Column
    @NotNull(message = "값을 입력하세요.")
    private LocalDate date;

    @Column
    @NotNull(message = "값을 입력하세요.")
    private Long catId;

    @Column
    @NotNull
    private Long sum;
}
