package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.dto.SalesStatisticsDTO;
import web.mvc.dto.ShopLikeDTO;
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

    @PostMapping("/shop")
    public ResponseEntity<List<ShopListDTO>> getShopItemsUser(@RequestBody long userSeq) {
        List<ShopListDTO> items = shopService.getAllShopItems(userSeq);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/daily")
    public ResponseEntity<List<SalesStatisticsDTO>> getDailySalesStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam Long stockSeq) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return ResponseEntity.ok(shopService.getDailySalesStatistics(startDateTime, endDateTime, stockSeq));
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<SalesStatisticsDTO>> getWeeklySalesStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam Long stockSeq) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return ResponseEntity.ok(shopService.getWeeklySalesStatistics(startDateTime, endDateTime, stockSeq));
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<SalesStatisticsDTO>> getMonthlySalesStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam Long stockSeq) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return ResponseEntity.ok(shopService.getMonthlySalesStatistics(startDateTime, endDateTime, stockSeq));
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, List<SalesStatisticsDTO>>> getAllSalesStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam Long stockSeq) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return ResponseEntity.ok(shopService.getAllSalesStatistics(startDateTime, endDateTime, stockSeq));
    }

    @PostMapping("/api/InsertShopLike")
    public ResponseEntity<Integer> getShopItemsUser(@RequestBody ShopLikeDTO shopLike) {
        shopService.insertShopLike(shopLike);
        return ResponseEntity.ok(0);
    }
    @GetMapping("/price/statistics")
    public ResponseEntity<Map<String, Integer>> getPriceStatistics(@RequestParam Long stockSeq) {
        return ResponseEntity.ok(shopService.getPriceStatistics(stockSeq));
    }
}