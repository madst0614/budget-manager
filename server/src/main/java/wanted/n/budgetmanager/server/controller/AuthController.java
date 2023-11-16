package wanted.n.budgetmanager.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.n.budgetmanager.server.dto.AccessTokenDTO;
import wanted.n.budgetmanager.server.dto.AccessTokenRequestDTO;
import wanted.n.budgetmanager.server.dto.AccessTokenResponseDTO;
import wanted.n.budgetmanager.server.dto.RefreshTokenDTO;
import wanted.n.budgetmanager.server.service.AuthService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auths")
@Api(tags = "Auth API", description = "인증과 관련된 API")
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/token/access")
    @ApiOperation(value = "Access Token 발급", notes= "Refresh Token을 확인하여 Access Token을 발급해 줍니다.")
    public ResponseEntity<AccessTokenResponseDTO> issueNewAccessToken(
            @Valid @RequestBody AccessTokenRequestDTO accessTokenRequestDTO) {

        // Access Token 발급 서비스 호출
        AccessTokenDTO accessTokenDTO =
                authService.issueNewAccessToken(RefreshTokenDTO.from(accessTokenRequestDTO));

        return new ResponseEntity<>(AccessTokenResponseDTO.from(accessTokenDTO), HttpStatus.OK);
    }
}
