package web.mvc.service;

import web.mvc.domain.UserBuy;
import web.mvc.dto.UserBuyReq;

public interface PaymentService {
    UserBuy paymentInsert (UserBuyReq userBuyReq);
}
