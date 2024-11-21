package web.mvc.service;

import web.mvc.domain.UserCharge;

public interface UserChargeService {
    /**
     * 지갑 충전 주문 및 결제 DB에 넣기
     */
    public UserCharge order (UserCharge userCharge);

    /**
     * 주문번호 생성
     */
    public String generateOrderUid();

    /**
     * 지갑 실제 충전
     */
    public String chargeWallet();
}
