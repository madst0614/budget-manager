package wanted.n.budgetmanager.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.n.budgetmanager.server.domain.User;
import wanted.n.budgetmanager.server.dto.UserSignInRequestDTO;
import wanted.n.budgetmanager.server.dto.UserSignInResponseDTO;
import wanted.n.budgetmanager.server.dto.UserSignUpRequestDTO;
import wanted.n.budgetmanager.server.exception.CustomException;
import wanted.n.budgetmanager.server.exception.ErrorCode;
import wanted.n.budgetmanager.server.repository.UserRepository;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 회원가입을 처리하는 메서드
     *  email 중복 체크 하여 중복 시 이메일 중복 exception
     */
    @Transactional
    public void signUpUser(UserSignUpRequestDTO userSignUpRequestDTO) {

        String password = userSignUpRequestDTO.getPassword();

        // 계정 중복 체크
        userRepository.findByEmail(userSignUpRequestDTO.getEmail())
                .ifPresent(user -> {
                    throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
                });

        // 비밀번호를 암호화하여 저장
        userSignUpRequestDTO.setPassword(passwordEncoder.encode(password));
        userRepository.save(User.from(userSignUpRequestDTO));
    }

    /**
     * 사용자 로그인 처리를 하는 메서드
     *  비밀번호를 비교하고 액세스 토큰 및 리프레시 토큰을 생성하여 사용자 정보를 반환합니다.
     */
    @Transactional
    public UserSignInResponseDTO signInUser(UserSignInRequestDTO signInRequest) {
        User user = userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.getDeleted()) {
            throw new CustomException(ErrorCode.USER_DELETED);
        }

        isPasswordMatch(signInRequest.getPassword(), user.getPassword());

        String accessToken = authService.signInAccessToken(user);
        String refreshToken = authService.issueNewRefreshToken(user.getId());

        return UserSignInResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 사용자 로그아웃 처리를 하는 메서드
     *  토큰으로부터 id를 추출하여 refresh 토큰을 제거합니다
     */
    @Transactional
    public void signOutUser(String token) {
        Long id = authService.getUserIdFromToken
                        (token);

        authService.deleteRefreshToken(id);
    }


    // 비밀번호 일치 여부를 확인하는 메소드
    private void isPasswordMatch(String password, String encodedPassword) {
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }
}
