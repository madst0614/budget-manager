package wanted.n.budgetmanager.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.n.budgetmanager.server.config.provider.JwtProvider;
import wanted.n.budgetmanager.server.domain.User;
import wanted.n.budgetmanager.server.dto.AccessTokenDTO;
import wanted.n.budgetmanager.server.dto.RefreshTokenDTO;
import wanted.n.budgetmanager.server.dto.TokenIssuanceDTO;
import wanted.n.budgetmanager.server.exception.CustomException;
import wanted.n.budgetmanager.server.exception.ErrorCode;
import wanted.n.budgetmanager.server.repository.UserRepository;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    /**
     *  Access Token 발급 메소드
     *  기능
     *      RefreshToken을 확인해서 Access Token을 발급해줍니다.
     *      Refresh Token의 id를 추출해서 Access Token에 API 이용에 필요한 정보들을 넣어줍니다.
     */
    @Transactional
    public AccessTokenDTO issueNewAccessToken(RefreshTokenDTO refreshTokenDTO) {
        String refreshToken = refreshTokenDTO.getRefreshToken();

        if(jwtProvider.validateToken(refreshToken) || !isRefreshTokenInServer(refreshToken))
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        Long id = jwtProvider.getIdFromToken(refreshToken);

        User user = userRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken =
                jwtProvider.generateAccessToken(TokenIssuanceDTO.from(user));

        return AccessTokenDTO.builder()
                .accessToken(newAccessToken)
                .build();
    }

    /**
     *  로그인 시 Access Token 발급 메소드
     */
    @Transactional
    public String signInAccessToken(User user) {

        return jwtProvider.generateAccessToken(TokenIssuanceDTO.from(user));
    }

    /**
     *  Refresh Token 발급 메소드
     *  기능
     *      Refresh Token을 발급해줍니다.
     *      <Key, Value> 형식으로 <id, Refresh Token>을 Redis 서버에 저장합니다.
     */
    @Transactional
    public String issueNewRefreshToken(Long id) {
        String refreshToken = jwtProvider.generateRefreshToken(id);

        redisService.saveRefreshToken(id, refreshToken);

        return refreshToken;
    }

    /**
     *  Refresh Token 정합성 검사 메소드
     *  기능
     *      Refresh Token이 레디스에 있는 값과 일치하는지 확인합니다.
     */
    public Boolean isRefreshTokenInServer(String refreshToken) {
        String serverToken = redisService.getRefreshToken(getUserIdFromToken(refreshToken));

        // 레디스에 있는 값과 일치하면 true 반환
        return refreshToken.equals(serverToken);
    }

    /**
     * 토큰으로부터 Id 추출
     */

    public Long getUserIdFromToken(String token){
        return jwtProvider.getIdFromToken(token);
    }

    /**
        Refresh Token 삭제 메소드
     */
    public void deleteRefreshToken(Long id) {

        redisService.deleteRefreshToken(id);
    }

}
