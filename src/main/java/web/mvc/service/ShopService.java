package web.mvc.service;

import web.mvc.domain.Shop;
import web.mvc.domain.Stock;
import web.mvc.dto.StockDTO;


public interface ShopService {
    /**
     * 상품 추가
     * **/
    int shopInsert(StockDTO stock);
    /**
     * 상품 수정
     * **/
    int shopUpdate(Shop shop);

    /**
     * 상품 삭제
     * **/
    int shopDelete(Long code);
}
