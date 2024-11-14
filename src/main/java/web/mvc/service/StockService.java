package web.mvc.service;

import web.mvc.domain.Product;
import web.mvc.domain.Stock;

public interface StockService {
    /**
     * 상품 등록
     */
    public Stock addStock(Stock stock);

    /**
     * 상품 조회
     */
    public Stock findAllProducts();

    /**
     * 상품 수정
     */
    public int updateStock(Stock stock);

    /**
     * 상품 삭제
     */
    public int deleteStock(Stock stock);
}
