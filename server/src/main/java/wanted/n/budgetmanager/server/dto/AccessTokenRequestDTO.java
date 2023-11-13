package wanted.n.budgetmanager.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessTokenRequestDTO {
    @NotBlank(message = "refreshToken을 입력해주세요.")
    private String refreshToken;
}
