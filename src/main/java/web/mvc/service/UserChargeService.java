package web.mvc.service;

import web.mvc.domain.UserCharge;

public interface UserChargeService {
    /**
     * 지갑 충전
     */
    public UserCharge order (UserCharge userCharge);

    /**
     * 주문번호 생성
     */
    public String generateOrderUid();
}
