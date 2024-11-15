package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.*;
import web.mvc.dto.StockDTO;
import web.mvc.service.StockService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StockController {

    private final ModelMapper modelMapper;

    private final StockService stockService;

    // 상품 등록
    @PostMapping("/stocks/stock")
    public ResponseEntity<?> insert(int productSeq, int stockGradeSeq, int stockOrganicSeq, long farmerSeq, @RequestBody StockDTO stockDTO) {
        log.info("Controller Product : {}", stockDTO);

        Stock stock = modelMapper.map(stockDTO, Stock.class);
        stock.setProduct(new Product(productSeq));
        stock.setStockGrade(new StockGrade(stockGradeSeq));
        stock.setStockOrganic(new StockOrganic(stockOrganicSeq));
        stock.setFarmerUser(new FarmerUser(farmerSeq));

        log.info("Stock 정보 : {}", stock);
        Stock result = stockService.addStock(stock);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // 상품 조회
    @GetMapping("/stocks/list")
    public ResponseEntity<?> findStocksById(int farmerId) {
        log.info("상품 목록 조회");

        List<Stock> stockList = stockService.findStocksById(farmerId);
        return new ResponseEntity<>(stockList, HttpStatus.OK);
    }

    // 상품 수정
    @PutMapping("/stocks/{id}")
    //public ResponseEntity<?> updateForm(Integer productCategorySeq, Integer productSeq, Integer stockGradeSeq, Integer stockOrganicSeq, Long farmerUserSeq, @RequestBody StockDTO stockDTO) {
    public ResponseEntity<?> updateForm(@PathVariable int id, @RequestBody StockDTO stockDTO) {
        System.out.println(stockDTO.getProductDTO().getProductSeq());
        System.out.println(stockDTO.getProductDTO().getProductCategoryDTO().getProductCategorySeq());
        System.out.println(stockDTO.getStockGradeDTO().getStockGradeSeq());
        System.out.println(stockDTO.getStockOrganicDTO().getStockOrganicSeq());

        Stock stock = modelMapper.map(stockDTO, Stock.class);

//        stock.setProduct(new Product(productSeq));
//        stock.setStockGrade(new StockGrade(stockGradeSeq));
//        stock.setStockOrganic(new StockOrganic(stockOrganicSeq));
//        stock.setFarmerUser(new FarmerUser(farmerUserSeq));
//        stock.getProduct().setProductCategory(new ProductCategory(productCategorySeq));

        log.info("Stock updateForm : {}", stock);
        return new ResponseEntity<>(stockService.updateStock(id, stock), HttpStatus.OK);
    }

    // 상품 삭제
    @DeleteMapping("/stocks/{id}")
    public ResponseEntity<?> deleteStock (@PathVariable int id) {
        log.info("삭제 컨트롤러");

        stockService.deleteStock(id);
        return new ResponseEntity<>(id +"번 재고 삭제완료", HttpStatus.OK);
    }
}
