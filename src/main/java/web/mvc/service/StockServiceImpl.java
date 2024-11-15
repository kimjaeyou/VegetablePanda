package web.mvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.mvc.domain.Product;
import web.mvc.domain.Stock;
import web.mvc.repository.StockRepository;

import java.util.List;

@Service
@Slf4j
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
    public List<Stock> findProductsById(int id) {
        stockRepository.findById(id);
        return null;
    }

    @Override
    public Stock updateStock(Stock stock) {
        log.info("updateStock call...");

        Stock dbStock = stockRepository.findById(stock.getStockSeq()).orElseThrow(null);
        dbStock.setProduct(stock.getProduct());
        dbStock.setCount(stock.getCount());
        dbStock.setContent(stock.getContent());
        stockRepository.save(dbStock);
        return dbStock;
    }

    @Override
    public int deleteStock(Stock stock) {
        return 0;
    }
}
