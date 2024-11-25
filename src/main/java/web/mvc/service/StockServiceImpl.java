package web.mvc.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.mvc.domain.Product;
import web.mvc.domain.ProductCategory;
import web.mvc.domain.Stock;
import web.mvc.exception.ErrorCode;
import web.mvc.exception.ProductException;
import web.mvc.exception.StockException;
import web.mvc.repository.StockRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class StockServiceImpl implements StockService {

    @Autowired
    StockRepository stockRepository;

    @Override
    public Stock addStock(Stock stock) {
        log.info("addProduct service 동작");
        log.info("Product : {}", stock);
        stock.setStatus(2);
        return stockRepository.save(stock);
    }

    @Override
    public List<Stock> findStocksById(long farmerSeq) {
        List<Stock> stockList = stockRepository.findStocksById(farmerSeq);
        log.info("");
        return stockList;
    }

    @Override
    //public Stock updateStock(long farmerUserSeq, int id, Stock stock) {
    public Stock updateStock(long id, Stock stock) {
        log.info("updateStock call... / stock.getStockSeq()={}", stock.getStockSeq());

        // 예외처리 필요
        Stock dbStock = stockRepository.findById(id).orElseThrow(()-> new StockException(ErrorCode.STOCK_UPDATE_FAILED));

        // Product 넣기
        dbStock.setProduct(stock.getProduct());
        // ProductCategory 넣기
        dbStock.getProduct().setProductCategory(stock.getProduct().getProductCategory());

        dbStock.setCount(stock.getCount());
        dbStock.setContent(stock.getContent());
        dbStock.setColor(stock.getColor());

       // stockRepository.save(dbStock);
        return dbStock;
    }

    @Override
    public int deleteStock(long id) {
        Stock stock = stockRepository.findById(id).orElseThrow(()-> new StockException(ErrorCode.STOCK_NOTFOUND));
        log.info("delect stock : {}", stock);
        stockRepository.deleteById(id);
        return 1;
    }

    @Override
    public List<Stock> findPendingStocks() {
        return stockRepository.findByStatus(2); // 2는 승인 대기 상태
    }

    @Override
    public Stock approveStock(long stockSeq) {
        Stock stock = stockRepository.findById(stockSeq)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        if (stock.getStatus() != 2) {
            throw new RuntimeException("Stock is not in pending state");
        }

        stock.setStatus(1); // 1은 승인된 상태
        return stockRepository.save(stock);
    }
}
