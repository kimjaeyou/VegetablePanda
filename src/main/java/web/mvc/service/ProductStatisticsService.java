package web.mvc.service;

import web.mvc.dto.DailyStatsDTO;
import web.mvc.dto.ProductStatisticsDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductStatisticsService {
    List<ProductStatisticsDTO> getProductSalesStatistics(LocalDateTime startDate, LocalDateTime endDate);

    List<DailyStatsDTO> getDailyStats(LocalDateTime startDate, LocalDateTime endDate);
}
