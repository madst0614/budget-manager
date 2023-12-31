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
    DATA_CONSISTENCY_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "데이터에 문제가 있습니다"),

    //User Exception
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),
    USER_DELETED(HttpStatus.BAD_REQUEST, "탈퇴한 사용자입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일 입니다"),

    //Token Exception
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),

    //Budget Exception
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    INVALID_BUDGET_DETAIL(HttpStatus.BAD_REQUEST, "Budget Detail 양식이 잘못됐습니다."),
    BUDGET_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 예산 정보가 존재하지 않습니다."),
    BUDGET_DELETED(HttpStatus.BAD_REQUEST, "삭제된 예산입니다."),
    BUDGET_EXISTS(HttpStatus.BAD_REQUEST, "해당 기간에 예산이 존재합니다."),

    //Spending Exception
    SPENDING_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 지출 정보가 존재하지 않습니다."),
    SPENDING_DELETED(HttpStatus.BAD_REQUEST, "삭제된 지출 입니다."),
    INVALID_SPENDING_UPDATE_DATE(HttpStatus.BAD_REQUEST, "날짜는 변경할 수 없습니다."),

    //Stats Exception
    NO_ENOUGH_DATA(HttpStatus.SERVICE_UNAVAILABLE, "데이터가 충분하지 않습니다"),

    //Category Exception
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}