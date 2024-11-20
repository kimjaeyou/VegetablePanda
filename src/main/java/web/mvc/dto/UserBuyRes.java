package web.mvc.dto;

import lombok.*;
import web.mvc.domain.Stock;
import web.mvc.domain.User;
import web.mvc.domain.UserBuy;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBuyRes {
    private Integer buySeq;
    private User user;
    private LocalDateTime buyDate;
    private Integer state;
    private Integer stockPrice;
    private Integer stockDiscount;

    // UserBuy Entity를 UserBuyRes로 변환
    public UserBuyRes (UserBuy userBuy) {
        buySeq = userBuy.getBuySeq();
        user = userBuy.getUser();
        buyDate = userBuy.getBuyDate();
        state = userBuy.getState();
        stockPrice = userBuy.getStockPrice();
        stockDiscount = userBuy.getStockDiscount();
    }
}
