package web.mvc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import web.mvc.domain.*;
import web.mvc.repository.StockGradeRepository;
import web.mvc.repository.StockOrganicRepository;
import web.mvc.repository.StockRepository;

@SpringBootTest
@Rollback(false)
@Slf4j
public class StockTest {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockGradeRepository stockGradeRepository;
    @Autowired
    private StockOrganicRepository stockOrganicRepository;
    //private Stock stock;

    /**
     * 재고 등급, 유기농 분류 등록
     */
    @Test
    @Rollback(false)
    @Disabled
    public void stockDataInsert(){
        stockGradeRepository.save(StockGrade.builder().grade("1등급").build());
        stockGradeRepository.save(StockGrade.builder().grade("2등급").build());
        stockGradeRepository.save(StockGrade.builder().grade("3등급").build());
        stockGradeRepository.save(StockGrade.builder().grade("4등급").build());

        stockOrganicRepository.save(StockOrganic.builder().organicStatus("유기농산물").build());
        stockOrganicRepository.save(StockOrganic.builder().organicStatus("전환기 유기농산물").build());
        stockOrganicRepository.save(StockOrganic.builder().organicStatus("무농약 농산물").build());
        stockOrganicRepository.save(StockOrganic.builder().organicStatus("저농약 농산물").build());
        stockOrganicRepository.save(StockOrganic.builder().organicStatus("분류없음").build());
    }

    /**
     * 재고 유기능 분류 등록
     */
    @Test
    @Rollback(false)
    @Disabled
    public void stockOrganicInsert(){
        stockOrganicRepository.save(StockOrganic.builder().organicStatus("유기농산물").build());
        stockOrganicRepository.save(StockOrganic.builder().organicStatus("전환기 유기농산물").build());
        stockOrganicRepository.save(StockOrganic.builder().organicStatus("무농약 농산물").build());
        stockOrganicRepository.save(StockOrganic.builder().organicStatus("저농약 농산물").build());
        stockOrganicRepository.save(StockOrganic.builder().organicStatus("분류없음").build());
    }

    /**
     * 재고 샘플 등록
     * ** 등록 전에 ProductCategory, Product, StockGrade, StockOrganic 정보 확인 필수
     */
    @Test
    @Rollback(value = false)
    @Disabled
    public void stockInsert () {
        //stock.setProduct(new Product(1));

        log.info("Stock 정보 : ");
        //stockRepository.save(Stock.builder().content("알감자").count(500).product(new Product(2L)).stockGrade(new StockGrade(1L)).stockOrganic(new StockOrganic(1L)).farmerUser(new FarmerUser(1)).color(1).build());
        //stockRepository.save(Stock.builder().content("알감자").count(500).product(new Product(2L)).stockGrade(new StockGrade(1L)).stockOrganic(new StockOrganic(1L)).farmerUser(new FarmerUser(1L)).color(1).build());

        stockRepository.save(Stock.builder()
                .content("알감자")
                .count(500)
                .status(2)
                .color(1)
                .product(Product.builder().productSeq(1L).build())
                .stockGrade(StockGrade.builder().stockGradeSeq(1L).build())
                .stockOrganic(StockOrganic.builder().stockOrganicSeq(1L).build())
                .farmerUser(FarmerUser.builder().userSeq(2L).build())
                .build());
    }
}
//package web.mvc;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import web.mvc.domain.*;
//import web.mvc.repository.StockGradeRepository;
//import web.mvc.repository.StockOrganicRepository;
//import web.mvc.repository.StockRepository;
//
//@SpringBootTest
//@Rollback(false)
//@Slf4j
//public class StockTest {
//
//    @Autowired
//    private StockRepository stockRepository;
//    @Autowired
//    private StockGradeRepository stockGradeRepository;
//    @Autowired
//    private StockOrganicRepository stockOrganicRepository;
//    //private Stock stock;
//
//    /**
//     * 재고 등급, 유기농 분류 등록
//     */
//    @Test
//    @Rollback(false)
//    @Disabled
//    public void stockDataInsert(){
//        stockGradeRepository.save(StockGrade.builder().grade("1등급").build());
//        stockGradeRepository.save(StockGrade.builder().grade("2등급").build());
//        stockGradeRepository.save(StockGrade.builder().grade("3등급").build());
//        stockGradeRepository.save(StockGrade.builder().grade("4등급").build());
//
//        stockOrganicRepository.save(StockOrganic.builder().organicStatus("유기농산물").build());
//        stockOrganicRepository.save(StockOrganic.builder().organicStatus("전환기 유기농산물").build());
//        stockOrganicRepository.save(StockOrganic.builder().organicStatus("무농약 농산물").build());
//        stockOrganicRepository.save(StockOrganic.builder().organicStatus("저농약 농산물").build());
//        stockOrganicRepository.save(StockOrganic.builder().organicStatus("분류없음").build());
//    }
//
//    /**
//     * 재고 유기능 분류 등록
//     */
//    @Test
//    @Rollback(false)
//    @Disabled
//    public void stockOrganicInsert(){
//        stockOrganicRepository.save(StockOrganic.builder().organicStatus("유기농산물").build());
//        stockOrganicRepository.save(StockOrganic.builder().organicStatus("전환기 유기농산물").build());
//        stockOrganicRepository.save(StockOrganic.builder().organicStatus("무농약 농산물").build());
//        stockOrganicRepository.save(StockOrganic.builder().organicStatus("저농약 농산물").build());
//        stockOrganicRepository.save(StockOrganic.builder().organicStatus("분류없음").build());
//    }
//
//    /**
//     * 재고 샘플 등록
//     * ** 등록 전에 ProductCategory, Product, StockGrade, StockOrganic 정보 확인 필수
//     */
//    @Test
//    @Rollback(value = false)
//    @Disabled
//    public void stockInsert () {
//        //stock.setProduct(new Product(1));
//
//        log.info("Stock 정보 : ");
//        //stockRepository.save(Stock.builder().content("알감자").count(500).product(new Product(2L)).stockGrade(new StockGrade(1L)).stockOrganic(new StockOrganic(1L)).farmerUser(new FarmerUser(1)).color(1).build());
//        //stockRepository.save(Stock.builder().content("알감자").count(500).product(new Product(2L)).stockGrade(new StockGrade(1L)).stockOrganic(new StockOrganic(1L)).farmerUser(new FarmerUser(1L)).color(1).build());
//
//        stockRepository.save(Stock.builder()
//                .content("알감자")
//                .count(500)
//                .status(2)
//                .color(1)
//                .product(Product.builder().productSeq(1L).build())
//                .stockGrade(StockGrade.builder().stockGradeSeq(1L).build())
//                .stockOrganic(StockOrganic.builder().stockOrganicSeq(1L).build())
//                .farmerUser(FarmerUser.builder().userSeq(2L).build())
//                .build());
//    }
//}
