package web.mvc.dto;

import lombok.*;
import web.mvc.domain.UserBuyDetail;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserBuyDetailDTO {
    private Long userBuySeq;
    private Long buySeq; // UserBuy의 ID (예시)
    private Integer price;
    private Integer count;
    private Long stockSeq; // stock_seq를 사용

    public UserBuyDetailDTO(UserBuyDetail userBuyDetail) {
        this.userBuySeq = userBuyDetail.getUserBuySeq();
        this.buySeq = userBuyDetail.getUserBuy().getBuySeq();
        this.price = userBuyDetail.getPrice();
        this.count = userBuyDetail.getCount();
        this.stockSeq = userBuyDetail.getStock().getStockSeq();
    }
}