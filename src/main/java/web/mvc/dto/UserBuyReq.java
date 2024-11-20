package web.mvc.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import web.mvc.domain.Stock;
import web.mvc.domain.User;
import web.mvc.domain.UserBuy;

@Setter
@Getter
@ToString
public class UserBuyReq {
    private Integer buySeq;
    private Long userSeq; // user pk
    private Integer state;
    private Integer stockPrice;
    private Integer stockDiscount;

    public UserBuy toUserBuy (UserBuyReq userBuyReq) {
        // UserBuyReq를 UserBuy 엔티티로 변환
        return UserBuy.builder().buySeq(userBuyReq.buySeq).user(User.builder().userSeq(userBuyReq.userSeq).build()).state(userBuyReq.state)
                .stockPrice(userBuyReq.state).stockDiscount(userBuyReq.stockDiscount).build();
    }

}
