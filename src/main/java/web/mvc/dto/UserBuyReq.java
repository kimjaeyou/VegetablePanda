package web.mvc.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.Stock;
import web.mvc.domain.User;
import web.mvc.domain.UserBuy;

import java.util.List;

@Setter
@Getter
@ToString
public class UserBuyReq {
    private Long buySeq;
    private Long userSeq; // user pk
    private Integer state;
    private Integer totalPrice;
    private String orderUid;

    private List<UserBuyDetailDTO> userBuyDetailDTOs;

    public UserBuy toUserBuy (UserBuyReq userBuyReq) {
        // UserBuyReq를 UserBuy 엔티티로 변환
        return UserBuy.builder().buySeq(userBuyReq.buySeq).managementUser(ManagementUser.builder().userSeq(userBuyReq.userSeq).build()).state(userBuyReq.state)
                .totalPrice(userBuyReq.state).orderUid(userBuyReq.getOrderUid()).build();
    }

}
