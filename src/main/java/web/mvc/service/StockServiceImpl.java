package web.mvc.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.mvc.domain.Product;
import web.mvc.domain.ProductCategory;
import web.mvc.domain.Stock;
import web.mvc.dto.AllStockDTO;
import web.mvc.dto.StockInfoDTO;
import web.mvc.exception.ErrorCode;
import web.mvc.exception.ProductException;
import web.mvc.exception.StockException;
import web.mvc.repository.StockRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@DynamicUpdate
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Override
    public Stock addStock(Stock stock) {
        log.info("addProduct service 동작");
        log.info("Product : {}", stock);
        stock.setStatus(0);
        stock.setRegDate(LocalDateTime.now());
        return stockRepository.save(stock);
    }

    @Override
    public AllStockDTO findAuctionStocksById(long farmerSeq) {
        AllStockDTO stock = stockRepository.findAuctionStocksById(farmerSeq);
        log.info("");
        return stock;    }

    @Override
    public Stock findStockById(long id) {
        Stock stock = stockRepository.findById(id).orElseThrow(() -> new StockException(ErrorCode.STOCK_NOTFOUND));
        return stock;
    }

    @Override
    public List<Stock> findStocksById(long farmerSeq) {
        List<Stock> stockList = stockRepository.findStocksById(farmerSeq);
        log.info("재고 유저 아이디 조회 : {}", stockList.get(0).getFarmerUser().getUserSeq());
        log.info("아이디로 재고 조회 {}", stockList);
        return stockList;
    }

    @Override
    //public Stock updateStock(long farmerUserSeq, int id, Stock stock) {
    public Stock updateStock(long id, Stock stock) {
        log.info("updateStock call... / stock.getStockSeq()={}", stock.getStockSeq());
        log.info("stock.getStockGrade = {}", stock.getStockGrade().getStockGradeSeq());

        // 예외처리 필요
        Stock dbStock = stockRepository.findById(id).orElseThrow(()-> new StockException(ErrorCode.STOCK_UPDATE_FAILED));

        // Product 넣기
        dbStock.setProduct(stock.getProduct());
        // ProductCategory 넣기
        dbStock.getProduct().setProductCategory(stock.getProduct().getProductCategory());

        // Grade, Organic 변경
        dbStock.setStockGrade(stock.getStockGrade());
        dbStock.setStockOrganic(stock.getStockOrganic());

        // File 변경
        dbStock.getFile().setFileSeq(stock.getFile().getFileSeq());

        dbStock.setCount(stock.getCount());
        dbStock.setContent(stock.getContent());
        dbStock.setColor(stock.getColor());

        return stockRepository.save(dbStock);
    }

    @Override
    public int deleteStock(long id) {
        Stock stock = stockRepository.findById(id).orElseThrow(()-> new StockException(ErrorCode.STOCK_NOTFOUND));
        log.info("delect stock : {}", stock);
        stock.setStatus(4);
        //stockRepository.deleteById(id);
        return 1;
    }

    @Override
    public List<Stock> findPendingStocks() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        return stockRepository.findByStatusAndRegDateAfter(0, oneDayAgo);
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

    @Override
    public void approveAllPendingStocks() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        List<Stock> pendingStocks = stockRepository.findByStatusAndRegDateAfter(0, oneDayAgo);

        for (Stock stock : pendingStocks) {
            stock.setStatus(1); // 승인 상태로 변경
            stockRepository.save(stock);
        }
    }

    @Override
    public boolean hasRegisteredToday(long farmerSeq) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        return stockRepository.existsByFarmerAndDateRange(farmerSeq, startOfDay, endOfDay);
    }

    @Override
    public Stock changeQuantity(long stockSeq, int quantity) {
        Stock stock = stockRepository.findById(stockSeq).orElseThrow(()-> new StockException(ErrorCode.STOCK_NOTFOUND));
        stock.setCount(stock.getCount()-quantity);
        return stock;
    }

    @Override
    public List<Stock> findStocksByFarmerSeq(Long farmerSeq) {
        return stockRepository.findByFarmerUserSeq(farmerSeq);
    }

    @Override
    public List<StockInfoDTO> findStockInfoById(long id) {

        return stockRepository.findStockInfoById(id);
    }
}
