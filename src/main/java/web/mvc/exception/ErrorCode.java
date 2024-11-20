package web.mvc.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
/**
 * Enum(열거형)은 서로 관련된 상수들을 정의하여 편리하게 사용하기 위한 자료형이다.
 * https://jddng.tistory.com/305
 *
 * */
public enum ErrorCode { //enum은 'Enumeration' 의 약자로 열거, 목록 이라는 뜻

	DUPLICATED(HttpStatus.BAD_REQUEST , "Duplicate Id", " 아이디가 중복입니다."),
	WRONG_PASS( HttpStatus.BAD_REQUEST, "password wrong","비밀번호 오류입니다.."),
    NOTFOUND_USER(HttpStatus.NOT_FOUND, "Not Found User", "사용자를 찾을 수 없습니다."),

	NOTFOUND_NO(HttpStatus.NOT_FOUND, "Not Found Board SearchById","글번호를 확인하세요."),
    NOTFOUND_BOARD( HttpStatus.BAD_REQUEST, "Not Found Board All","전체 게시물을 조회 할수 없습니다."),

    UPDATE_FAILED( HttpStatus.BAD_REQUEST, "Update fail","수정할수 없습니다."),
    DELETE_FAILED( HttpStatus.BAD_REQUEST, "Delete fail","삭제할 수 없습니다."),

    STOCK_UPDATE_FAILED (HttpStatus.BAD_REQUEST, "Stock update fail", "재고 수정에 실패했습니다."),
    STOCK_NOTFOUND (HttpStatus.NOT_FOUND, "Stock update fail", "재고를 찾을 수 없습니다."),

    PRODUCT_UPDATE_FAILED (HttpStatus.BAD_REQUEST, "Stock update fail", "상품 수정에 실패했습니다."),
    PRODUCT_NOTFOUND (HttpStatus.NOT_FOUND, "Stock update fail", "상품을 찾을 수 없습니다."),

    AUCTION_NOTFOUND (HttpStatus.NOT_FOUND, "Stock update fail", "경매 정보를 찾을 수 없습니다."),

    ORDER_NOTFOUND (HttpStatus.NOT_FOUND, "Not Found Order", "주문 정보가 없습니다.");


    private final HttpStatus httpStatus;
    private  final String title;
    private final String message;
}



