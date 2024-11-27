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
import web.mvc.dto.AllStockDTO;
import web.mvc.dto.FarmerUserDTO;
import web.mvc.dto.StockDTO;
import web.mvc.service.StockService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StockController {

    private final ModelMapper modelMapper;
    private final StockService stockService;

    // 상품 등록 -> 상품 이미지 어떻게 하는지
    @PostMapping("/stock")
    //public ResponseEntity<?> insert(long productSeq, long stockGradeSeq, long stockOrganicSeq, long farmerSeq, @RequestBody StockDTO stockDTO) {
    public ResponseEntity<?> insert(long farmerSeq, @RequestBody StockDTO stockDTO) {
        log.info("Controller Product : {}", stockDTO);

        try {
            // 오늘 등록한 상품이 있는지 확인
            if (stockService.hasRegisteredToday(farmerSeq)) {
                return new ResponseEntity<>("하루에 한 상품만 등록할 수 있습니다.", HttpStatus.BAD_REQUEST);
            }

            Stock stock = modelMapper.map(stockDTO, Stock.class);
    //        stock.setProduct(new Product(productSeq));
    //        stock.setStockGrade(new StockGrade(stockGradeSeq));
    //        stock.setStockOrganic(new StockOrganic(stockOrganicSeq));
            stock.setProduct(new Product(stockDTO.getProductSeq()));
            stock.setStockGrade(new StockGrade(stockDTO.getStockGradeSeq()));
            stock.setStockOrganic(new StockOrganic(stockDTO.getStockOrganicSeq()));
            stock.setFarmerUser(new FarmerUser(farmerSeq));

            log.info("Stock 정보 : {}", stock);

            StockDTO result = modelMapper.map(stockService.addStock(stock), StockDTO.class);
            return new ResponseEntity<>(result, HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("상품 등록 실패: ", e);
            return new ResponseEntity<>("상품 등록에 실패했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 상품 조회 (판매자 재고 보기)
    @GetMapping("/stock/{userSeq}")
    public ResponseEntity<?> findStocksById(@PathVariable long userSeq) {
        log.info("상품 목록 조회");

        List<Stock> stockList = stockService.findStocksById(userSeq);
        List<StockDTO> stockDTOList = stockList.stream().map(data -> modelMapper.map(data, StockDTO.class)).toList();
        System.out.println("stocklist 값" + stockList);
        return new ResponseEntity<>(stockDTOList, HttpStatus.OK);
    }

    // 상품 조회 (판매자 재고 보기)
    @GetMapping("/auctionStock/{userSeq}")
    public ResponseEntity<?> findAuctionStocksById(@PathVariable long userSeq) {
        log.info("상품 목록 조회");

        AllStockDTO stockDTO = stockService.findAuctionStocksById(userSeq);
        System.out.println("stock값" + stockDTO);
        return new ResponseEntity<>(stockDTO, HttpStatus.OK);
    }

    // 상품 수정 -> userId와 StockDTO 에 정보를 담아 가져간다
    @PutMapping("/stock")
    //public ResponseEntity<?> updateForm(Integer productCategorySeq, Integer productSeq, Integer stockGradeSeq, Integer stockOrganicSeq, Long farmerUserSeq, @RequestBody StockDTO stockDTO) {
    public ResponseEntity<?> update(@RequestBody StockDTO stockDTO) {
        // 값 확인용
        System.out.println(stockDTO.getProductSeq());
        System.out.println(stockDTO.getStockGradeSeq());
        System.out.println(stockDTO.getStockOrganicSeq());

        long id = stockDTO.getStockSeq();

        Stock stock = modelMapper.map(stockDTO, Stock.class);

        //stock.setFarmerUser(new FarmerUser(farmerSeq));

        StockDTO result = modelMapper.map(stockService.updateStock(id, stock), StockDTO.class);

        log.info("Stock update : {}", stock);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    // 상품 삭제
    @DeleteMapping("/stock/{id}")
    public ResponseEntity<?> deleteStock (@PathVariable int id) {
        log.info("삭제 컨트롤러");

        stockService.deleteStock(id);
        return new ResponseEntity<>(id +"번 재고 삭제완료", HttpStatus.OK);
    }

    @GetMapping("/stock/pending")
    public ResponseEntity<?> getPendingStocks() {
        List<Stock> pendingStocks = stockService.findPendingStocks();
        List<StockDTO> pendingStockDTOs = pendingStocks.stream()
                .map(stock -> modelMapper.map(stock, StockDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(pendingStockDTOs, HttpStatus.OK);
    }

    @PutMapping("/stock/approve/{stockSeq}")
    public ResponseEntity<?> approveStock(@PathVariable long stockSeq) {
        Stock approvedStock = stockService.approveStock(stockSeq);
        return new ResponseEntity<>(modelMapper.map(approvedStock, StockDTO.class), HttpStatus.OK);
    }


}