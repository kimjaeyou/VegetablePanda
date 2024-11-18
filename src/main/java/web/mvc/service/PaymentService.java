package web.mvc.service;

import web.mvc.domain.UserBuy;
import web.mvc.domain.UserCharge;
import web.mvc.dto.UserBuyReq;

import java.util.Map;

public interface PaymentService {
    public UserBuy paymentInsert (UserBuy userBuy);

    public int saveCharge(UserCharge userCharge);

    /**
     * 마지막 충전 내역 가져오기
     */
    public UserCharge getLastCharge();
}
