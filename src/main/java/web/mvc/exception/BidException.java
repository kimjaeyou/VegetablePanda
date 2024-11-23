package web.mvc.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BidException extends RuntimeException {
    private final ErrorCode errorCode;
}
