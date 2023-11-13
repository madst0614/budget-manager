package wanted.n.budgetmanager.server.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenDTO {
    private String refreshToken;

    public static RefreshTokenDTO from(AccessTokenRequestDTO accessTokenRequestDTO) {
        return RefreshTokenDTO.builder()
                .refreshToken(accessTokenRequestDTO.getRefreshToken())
                .build();
    }
}
