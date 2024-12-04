package web.mvc.dto;

import lombok.*;
import web.mvc.domain.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserBuyRes {
    private long buySeq;
    private ManagementUser managementUser;
    private LocalDateTime buyDate;
    private Integer state;
    private Integer totalPrice;
    private String orderUid;

    private List<UserBuyDetailDTO> userBuyDetails;


    // UserBuy Entity를 UserBuyRes로 변환
    public UserBuyRes (UserBuy userBuy) {
        buySeq = userBuy.getBuySeq();
        managementUser = userBuy.getManagementUser();
        buyDate = userBuy.getBuyDate();
        state = userBuy.getState();
        totalPrice = userBuy.getTotalPrice();
        orderUid = userBuy.getOrderUid();
        for(UserBuyDetail userBuyDetail : userBuy.getUserBuyDetailList()) {
            userBuyDetails.add(new UserBuyDetailDTO(userBuyDetail));
        }
    }
}
