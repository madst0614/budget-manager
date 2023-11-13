package wanted.n.budgetmanager.server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.n.budgetmanager.server.dto.UserSignUpRequestDTO;
import wanted.n.budgetmanager.server.enums.UserRole;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @NotNull
    @Column
    private String password;

    @NotNull
    @Column
    private String account;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @NotNull
    @Column
    private Boolean deleted;

    public static User from(UserSignUpRequestDTO userSignUpRequestDTO){
        return User.builder()
                .email(userSignUpRequestDTO.getEmail())
                .account(userSignUpRequestDTO.getAccount())
                .password(userSignUpRequestDTO.getPassword())
                .userRole(UserRole.ROLE_USER)
                .deleted(false)
                .build();

    }
}
