package web.mvc.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.mvc.domain.Product;
import web.mvc.domain.ProductCategory;
import web.mvc.domain.Stock;
import web.mvc.exception.ErrorCode;
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
        return stockRepository.save(stock);
    }

    @Override
    public List<Stock> findStocksById(long farmerSeq) {
        List<Stock> stockList = stockRepository.findStocksById(farmerSeq);
        log.info("");
        return stockList;
    }

    @Override
    public Stock updateStock(long farmerUserSeq, int id, Stock stock) {
        log.info("updateStock call... / stock.getStockSeq()={}", stock.getStockSeq());

        // 예외처리 필요
        Stock dbStock = stockRepository.findById(id).orElseThrow(()-> new StockException(ErrorCode.UPDATE_FAILED));

        // Product 넣기
        dbStock.setProduct(stock.getProduct());
        // ProductCategory 넣기
        dbStock.getProduct().setProductCategory(stock.getProduct().getProductCategory());

        dbStock.setCount(stock.getCount());
        dbStock.setContent(stock.getContent());

       // stockRepository.save(dbStock);
        return dbStock;
    }

    @Override
    public int deleteStock(int id) {
        Stock stock = stockRepository.findById(id).orElseThrow(null);
        log.info("delect stock : {}", stock);
        stockRepository.deleteById(id);
        return 0;
    }
}
