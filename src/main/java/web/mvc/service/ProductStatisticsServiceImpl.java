package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.dto.DailyStatsDTO;
import web.mvc.dto.ProductStatisticsDTO;
import web.mvc.repository.UserBuyDetailRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductStatisticsServiceImpl implements ProductStatisticsService {

    private final UserBuyDetailRepository userBuyDetailRepository;

    @Override
    public List<ProductStatisticsDTO> getProductSalesStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        return userBuyDetailRepository.findProductStats(startDate, endDate);
    }

    @Override
    public List<DailyStatsDTO> getDailyStats(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = userBuyDetailRepository.findDailyStats(startDate, endDate);

        return results.stream()
                .map(row -> DailyStatsDTO.builder()
                        .date((String) row[0])
                        .totalQuantity(((Number) row[1]).longValue())
                        .totalAmount(((Number) row[2]).longValue())
                        .build())
                .collect(Collectors.toList());
    }
}