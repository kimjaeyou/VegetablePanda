package web.mvc.service;

import web.mvc.domain.UserBuy;

public interface UserBuyService {
    public UserBuy buy(UserBuy userBuy);

    /**
     * 일반 물품 주문 정보 넣기 - 주문 내용
     */
    public UserBuy insertShopOrder(UserBuy userBuy);
}
