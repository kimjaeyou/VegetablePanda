package web.mvc.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.mvc.domain.Product;
import web.mvc.domain.ProductCategory;
import web.mvc.domain.Stock;
import web.mvc.repository.StockRepository;

import java.util.List;

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
    public List<Stock> findStocksById(long id) {
        List<Stock> stockList = stockRepository.findStocksById(id);
        log.info("");
        return null;
    }

    @Override
    public Stock updateStock(Stock stock) {
        log.info("updateStock call... / stock.getStockSeq()={}", stock.getStockSeq());

        Stock dbStock = stockRepository.findById(stock.getStockSeq()).orElseThrow(null);
        //dbStock.setProduct(stock.getProduct());
        // Product 넣기
        //dbStock.setProduct(new Product(stock.getProduct().getProductSeq()));
        dbStock.setProduct(stock.getProduct());
        //dbStock.getProduct().getProductCategory().setProductCategorySeq(stock.getProduct().getProductCategory().getProductCategorySeq());
        // ProductCategory 넣기
        //dbStock.getProduct().setProductCategory(new ProductCategory(stock.getProduct().getProductCategory().getProductCategorySeq()));
        //dbStock.getProduct().getProductCategory().setProductCategorySeq(stock.getProduct().getProductCategory().getProductCategorySeq());
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
