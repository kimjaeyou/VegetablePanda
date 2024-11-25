package web.mvc.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AuctionDTO2 {

    // 상품 번호
    private Long bidSeq;

    // 상품명
    private String content;

    // 입찰 날짜
    private String date;

    // 입찰 가격
    private Integer price;

    //수량
    private Integer count;

    // 입찰결과
    private Integer status;

    // 판매자 이름
    private String farmerName;
}
