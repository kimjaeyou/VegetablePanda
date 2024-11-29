package web.mvc.service;

import web.mvc.domain.UserBuy;
import web.mvc.dto.UserBuyListByStockDTO;

import java.util.List;

public interface UserBuyService {
    public UserBuy buy(UserBuy userBuy);

    List<UserBuyListByStockDTO> geUserBuyListByStockDtos(Long stockSeq);
}
