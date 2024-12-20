package web.mvc.service;

import web.mvc.domain.Product;
import web.mvc.domain.Stock;
import web.mvc.dto.AllStockDTO;
import web.mvc.dto.StockInfoDTO;

import java.util.List;

public interface StockService {
    /**
     * 상품 등록
     */
    public Stock addStock(Stock stock);

    /**
     * 상품 목록 조회
     */
    public List<Stock> findStocksById(long id);

    /**
     * 상품 조회 - stockInfo List 반환
     */
    public List<StockInfoDTO> findStockInfoById(long id);

    /**
     * 경매 재고 조회
     */
    public AllStockDTO findAuctionStocksById(long id);

    /**
     * 상품 조회 (1개)
     */
    public Stock findStockById(long id);

    /**
     * 상품 수정
     */
    //public Stock updateStock(long farmerUserSeq, int id, Stock stock);
    public Stock updateStock(long id, Stock stock);

    /**
     * 상품 삭제
     */
    public int deleteStock(long id);

    List<Stock> findPendingStocks();

    Stock approveStock(long stockSeq);

    void approveAllPendingStocks();

    boolean hasRegisteredToday(long farmerSeq);

    Stock changeQuantity(long stockSeq, int quantity);

    List<Stock> findStocksByFarmerSeq(Long farmerSeq);
}
