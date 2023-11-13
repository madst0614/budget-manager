package wanted.n.budgetmanager.server.config.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import wanted.n.budgetmanager.server.dto.TokenIssuanceDTO;
import wanted.n.budgetmanager.server.service.UserDetailsServiceImpl;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider{

    private final UserDetailsServiceImpl userDetailsService;

    private String issuer;
    private SecretKey secretKey;
    // Access Token, Refresh Token 만료시간
    private final int ATEXP = 5 * 60 * 1000;
    private final int RTEXP = 60 * 60 * 1000;


    /* SecretKey 초기화 메서드 (빈 생성 시 1회 실행) */
    @PostConstruct
    public void init() {
        issuer = "budget-manager";
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    /* Access Token 생성 메서드 - 클레임에 ID와 계정, UserRole 삽입 */
    public String generateAccessToken(TokenIssuanceDTO tokenIssuanceDTO) {
        Claims claims = Jwts.claims();
        claims.put("id", tokenIssuanceDTO.getId().toString());
        claims.put("account", tokenIssuanceDTO.getAccount());
        claims.put("userRole", tokenIssuanceDTO.getUserRole().getRoleName());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // 유효 : 5분
                .setExpiration(new Date(System.currentTimeMillis() + ATEXP))
                .signWith(secretKey)
                .compact();
    }

    /* Refresh Token 생성 메서드 - 클레임에 id 삽입 */
    public String generateRefreshToken(Long id) {
        return Jwts.builder()
                .claim("id", id.toString())
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // 유효 : 1시간
                .setExpiration(new Date(System.currentTimeMillis() + (RTEXP)))
                .signWith(secretKey)
                .compact();
    }

    /* 토큰 유효성 검증
    * 복호화 작업을 통해 암호화된 키가 서버가 갖고 있는 키인지 확인하고
    * 유효 기간을 체크한다
    *  */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims =
                    Jwts.parserBuilder()
                            .setSigningKey(secretKey)
                            .build()
                            .parseClaimsJws(token);

            log.info("<Jwt Provider> Token 유효 체크");
            log.info("<Jwt Provider> id: " + claims.getBody().get("id", String.class));
            log.info("<Jwt Provider 토큰 만료 시간: " + claims.getBody().getExpiration().toString());

            return claims.getBody().getExpiration().before(new Date());
        } catch (JwtException ex) {
            throw new IllegalStateException("올바르지 않은 토큰입니다.");
        }
    }

    /* 토큰에서 id 추출 */
    public Long getIdFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        return Long.parseLong(
                Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .get("id", String.class));
    }

    /* 토큰에서 계정 추출 */
    public String getAccountFromToken(String token) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("account", String.class);
    }

    /* Spring Context에서 처리할 인증 객체 생성 */
    public Authentication getAuthentication(String token) {

        // "Bearer " 프리픽스 제거
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 토큰을 파싱하여 클레임 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // claim의 email 이용해서 사용자 정보 로드
        UserDetails userDetails = userDetailsService.loadUserByUsername
                (claims.get("id", String.class));

        // 사용자 권한을 추출하여 Authentication 객체 생성
        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities()
        );
    }
}
