package wanted.n.budgetmanager.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessTokenResponseDTO {
    private String accessToken;

    public static AccessTokenResponseDTO from(AccessTokenDTO accessTokenDTO) {
        return AccessTokenResponseDTO.builder()
                .accessToken(accessTokenDTO.getAccessToken())
                .build();
    }
}
