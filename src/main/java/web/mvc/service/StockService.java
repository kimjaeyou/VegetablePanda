package web.mvc.service;

import web.mvc.domain.Product;
import web.mvc.domain.Stock;

import java.util.List;

public interface StockService {
    /**
     * 상품 등록
     */
    public Stock addStock(Stock stock);

    /**
     * 상품 조회
     */
    public List<Stock> findStocksById(long id);

    /**
     * 상품 수정
     */
    public Stock updateStock(Stock stock);

    /**
     * 상품 삭제
     */
    public int deleteStock(int id);
}
