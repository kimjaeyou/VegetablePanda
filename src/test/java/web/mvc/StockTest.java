package web.mvc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import web.mvc.domain.Product;
import web.mvc.domain.Stock;
import web.mvc.domain.StockGrade;
import web.mvc.domain.StockOrganic;
import web.mvc.repository.StockRepository;

@SpringBootTest
@Rollback(false)
@Slf4j
public class StockTest {

    @Autowired
    private StockRepository stockRepository;
    //private Stock stock;

    /**
     * 상품 샘플 등록
     */
    @Test
    @Rollback(value = false)
    @Disabled
    public void stockInsert () {
        //stock.setProduct(new Product(1));

        log.info("Stock 정보 : ");
        stockRepository.save(Stock.builder().content("알감자").count(500).product(Product.builder().productSeq(1).build()).stockGrade(StockGrade.builder().stockGradeSeq(1).build()).stockOrganic(StockOrganic.builder().stockOrganicSeq(1).build()).build());

    }
}
