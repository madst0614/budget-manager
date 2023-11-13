package wanted.n.budgetmanager.server.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.n.budgetmanager.server.dto.UserSignInRequestDTO;
import wanted.n.budgetmanager.server.dto.UserSignInResponseDTO;
import wanted.n.budgetmanager.server.dto.UserSignUpRequestDTO;
import wanted.n.budgetmanager.server.service.UserService;

import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Api(tags = "User API", description = "사용자와 관련된 API")
@RestController
public class UserController {

    private final UserService userService;
    @PostMapping("/sign-up")
    @ApiOperation(value = "회원가입", notes = "사용자가 회원정보를 입력하여 회원가입을 진행합니다.")
    public ResponseEntity<Void> signUp(
            @Valid @RequestBody UserSignUpRequestDTO userSignUpRequestDTO) {

        // 입력받은 정보로 회원가입
        userService.signUpUser(userSignUpRequestDTO);

        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/sign-in")
    @ApiOperation(value = "로그인", notes = "사용자가 입력한 정보로 로그인을 진행합니다.")
    public ResponseEntity<UserSignInResponseDTO> signIn(
            @Valid @RequestBody UserSignInRequestDTO signInRequest) {

        // 사용자 로그인 후 사용자 정보 및 토큰(엑세스/리프레시) 발급
        return ResponseEntity.status(OK).body( userService.signInUser(signInRequest));
    }

    @PostMapping("/sign-out")
    @ApiOperation(value = "로그아웃", notes = "사용자의 로그아웃을 진행합니다.")
    public ResponseEntity<Void> signOut(@RequestHeader(AUTHORIZATION) String token) {
        userService.signOutUser(token);

        return ResponseEntity.status(NO_CONTENT).build();
    }
}
