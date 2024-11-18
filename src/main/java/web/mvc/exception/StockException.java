package web.mvc.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StockException extends RuntimeException {
    private final ErrorCode errorCode;
}
