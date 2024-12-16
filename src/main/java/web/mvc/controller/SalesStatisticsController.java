package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.dto.DailyStatsDTO;
import web.mvc.dto.ProductStatisticsDTO;
import web.mvc.service.ProductStatisticsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class SalesStatisticsController {


    private final ProductStatisticsService productStatisticsService;  // 인터페이스 타입으로 변경

    @GetMapping("/products")
    public ResponseEntity<List<ProductStatisticsDTO>> getProductSalesStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDate) {

        List<ProductStatisticsDTO> statistics =
                productStatisticsService.getProductSalesStatistics(startDate, endDate);

        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/daily")
    public ResponseEntity<List<DailyStatsDTO>> getDailyStats(
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");

        List<DailyStatsDTO> stats = productStatisticsService.getDailyStats(start, end);
        return ResponseEntity.ok(stats);
    }
}