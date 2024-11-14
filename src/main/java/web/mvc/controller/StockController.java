package web.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("/stocks/list")
    public void stock() {
        log.info("상품 목록 조회");
    }

    // 상품 수정
    @PostMapping("/stock/updateForm")
    public ResponseEntity<?> updateForm(@RequestBody Stock stock) {
        log.info("Stock updateForm : {}", stock);
        return new ResponseEntity<>(stockService.updateStock(stock), HttpStatus.OK);
    }

    // 상품 삭제
}
