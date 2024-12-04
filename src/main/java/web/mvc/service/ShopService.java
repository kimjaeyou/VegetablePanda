package web.mvc.service;

import web.mvc.domain.Shop;
import web.mvc.domain.ShopLike;
import web.mvc.domain.Stock;
import web.mvc.dto.SalesStatisticsDTO;
import web.mvc.dto.ShopLikeDTO;
import web.mvc.dto.ShopListDTO;
import web.mvc.dto.StockDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


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

    void insertShopLike(Long userSeq,Long shopSeq);


    List<ShopListDTO> getAllShopItems(long seq);

    // 이것도 윤성이가 씀
    List<ShopListDTO> getShopItemsUser(long seq);

    // 이것도 윤성이가 씀


    List<SalesStatisticsDTO> getDailySalesStatistics(LocalDateTime startDate, LocalDateTime endDate);
    List<SalesStatisticsDTO> getDailySalesStatistics(LocalDateTime startDate, LocalDateTime endDate, Long stockSeq);
    List<SalesStatisticsDTO> getWeeklySalesStatistics(LocalDateTime startDate, LocalDateTime endDate, Long stockSeq);
    List<SalesStatisticsDTO> getMonthlySalesStatistics(LocalDateTime startDate, LocalDateTime endDate, Long stockSeq);
    Map<String, List<SalesStatisticsDTO>> getAllSalesStatistics(LocalDateTime startDate, LocalDateTime endDate, Long stockSeq);
    Map<String, Integer> getPriceStatistics(Long stockSeq);

    ShopLike getByUserSeqAndStockSeq(Long userSeq, Long shopSeq);

}

