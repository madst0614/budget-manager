package wanted.n.budgetmanager.server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private Long userId;

    @Column
    @NotNull(message = "값을 입력하세요.")
    private LocalDate date;

    @Column
    @NotNull
    private Boolean deleted;
}
