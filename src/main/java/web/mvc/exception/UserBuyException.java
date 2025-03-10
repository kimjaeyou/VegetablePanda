package web.mvc.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserBuyException extends RuntimeException {
    private final ErrorCode errorCode;
}
