package web.mvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.mvc.domain.Product;
import web.mvc.domain.Stock;
import web.mvc.repository.StockRepository;

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
    public Stock findAllProducts() {
        return null;
    }

    @Override
    public int updateStock(Stock stock) {
        return 0;
    }

    @Override
    public int deleteStock(Stock stock) {
        return 0;
    }
}
