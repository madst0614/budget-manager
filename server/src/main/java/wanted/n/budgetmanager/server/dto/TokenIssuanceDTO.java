package wanted.n.budgetmanager.server.dto;

import lombok.Builder;
import lombok.Getter;
import wanted.n.budgetmanager.server.domain.User;
import wanted.n.budgetmanager.server.enums.UserRole;


@Getter
@Builder
public class TokenIssuanceDTO {
    private String email;
    private Long id;
    private String account;
    private UserRole userRole;

    public static TokenIssuanceDTO from(User user){

        return TokenIssuanceDTO.builder()
                .email(user.getEmail())
                .id(user.getId())
                .account(user.getAccount())
                .userRole(user.getUserRole())
                .build();
    }
}
