package web.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.domain.Product;
import web.mvc.domain.Stock;
import web.mvc.service.StockService;

@RestController
@Slf4j
public class StockController {

    @Autowired
    StockService stockService;

    // 상품 등록
    @PostMapping("/stocks/stock")
    public ResponseEntity<?> insert(int productSeq, @RequestBody Stock stock) {
        log.info("Controller Product : {}", stock);
        stock.setProduct(new Product(productSeq));

        log.info("Stock 정보 : {}", stock);
        Stock result = stockService.addStock(stock);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // 상품 조회

    // 상품 수정

    // 상품 삭제
}
