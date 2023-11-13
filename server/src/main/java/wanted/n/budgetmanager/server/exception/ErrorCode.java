package wanted.n.budgetmanager.server.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    //GlobalException
    UNDEFINED_EXCEPTION(HttpStatus.BAD_REQUEST, "알 수 없는 오류입니다."),
    RESTAURANT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 맛집 정보를 찾을 수 없습니다."),
    INVALID_PAGINATION_OFFSET(HttpStatus.BAD_REQUEST, "page offset에 음수가 들어갈 수 없습니다."),
    INVALID_PAGINATION_SIZE(HttpStatus.BAD_REQUEST, "page size에 음수가 들어갈 수 없습니다."),

    //User Exception
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),
    USER_DELETED(HttpStatus.BAD_REQUEST, "탈퇴한 사용자입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일 입니다"),

    //Token Exception
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}