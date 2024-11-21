package web.mvc.dto;

import lombok.*;
import web.mvc.domain.ManagementUser;
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
    private long buySeq;
    private ManagementUser managementUser;
    private LocalDateTime buyDate;
    private Integer state;
    private Integer stockPrice;
    private Integer stockDiscount;

    // UserBuy Entity를 UserBuyRes로 변환
    public UserBuyRes (UserBuy userBuy) {
        buySeq = userBuy.getBuySeq();
        managementUser = userBuy.getManagementUser();
        buyDate = userBuy.getBuyDate();
        state = userBuy.getState();
        stockPrice = userBuy.getStockPrice();
        stockDiscount = userBuy.getStockDiscount();
    }
}
