package web.mvc.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBuyDTO {
    private Integer userBuySeq; // 주문번호
    private String content; // 상품명
    private Integer price; // 가격
    private Integer count; // 수량
    private String date; // 구매날짜
    private String name; // 이건 구매입장은 판매자가 들어갈꺼고 판매자 입장에선 누가 구매했는지 나올거에요

}
