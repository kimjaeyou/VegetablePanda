package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.dto.SalesStatisticsDTO;
import web.mvc.dto.ShopListDTO;
import web.mvc.service.ShopService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;

    @GetMapping("/shop")
    public ResponseEntity<List<ShopListDTO>> getShopItems() {
        List<ShopListDTO> items = shopService.getAllShopItems(0);
        return ResponseEntity.ok(items);
    }

    // 윤성이가 씀
    @PostMapping("/shop/{seq}")
    public ResponseEntity<List<ShopListDTO>> getShopItemsUser(@PathVariable Long seq) {
        return new ResponseEntity <> (shopService.getShopItemsUser(seq) , HttpStatus.OK);
    }

    // 윤성이가 씀
    @PostMapping("/shop/select/{seq}")
    public ResponseEntity<List<ShopListDTO>> getShopItemsUserSelect(@PathVariable Long seq) {
        return new ResponseEntity <> (shopService.getAllShopItems(seq) , HttpStatus.OK);
    }

    @GetMapping("/daily")
    public ResponseEntity<List<SalesStatisticsDTO>> getDailySalesStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        LocalDateTime startDateTime = startDate.atStartOfDay();  // 시작일 00:00:00
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);  // 종료일 23:59:59

        return ResponseEntity.ok(shopService.getDailySalesStatistics(startDateTime, endDateTime));
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<SalesStatisticsDTO>> getWeeklySalesStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return ResponseEntity.ok(shopService.getWeeklySalesStatistics(startDateTime, endDateTime));
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<SalesStatisticsDTO>> getMonthlySalesStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return ResponseEntity.ok(shopService.getMonthlySalesStatistics(startDateTime, endDateTime));
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, List<SalesStatisticsDTO>>> getAllSalesStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return ResponseEntity.ok(shopService.getAllSalesStatistics(startDateTime, endDateTime));
    }

    @GetMapping("/price/statistics")
    public ResponseEntity<Map<String, Integer>> getPriceStatistics() {
        return ResponseEntity.ok(shopService.getPriceStatistics());
    }
}