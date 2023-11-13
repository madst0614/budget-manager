package wanted.n.budgetmanager.server.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignInRequestDTO {
    String email;
    String password;
}
