package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesStatisticsDTO {
    private String period;
    private String productName;
    private Long totalSales;
    private Long totalQuantity;
    private Long totalAmount;
    private String periodType;
}