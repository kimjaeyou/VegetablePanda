package web.mvc.service;

import web.mvc.domain.UserBuy;
import web.mvc.domain.UserBuyDetail;
import web.mvc.dto.UserBuyListByStockDTO;

import java.util.List;
import java.util.Optional;

public interface UserBuyService {
    public UserBuy buy(UserBuy userBuy);

    List<UserBuyListByStockDTO> geUserBuyListByStockDtos(Long stockSeq);

    /**
     * 일반 물품 주문 정보 넣기 - 주문 내용
     */
    public UserBuy insertShopOrder(UserBuy userBuy);

    /**
     * 주문 삭제
     */
    public int deleteOrder(long userBuySeq);



    Optional<UserBuyDetail> findById(Long id);
}
