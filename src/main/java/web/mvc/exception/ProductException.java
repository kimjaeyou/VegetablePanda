package web.mvc.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductException extends RuntimeException {
    private final ErrorCode errorCode;
}
