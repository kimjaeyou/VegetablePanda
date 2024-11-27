package web.mvc.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyStatsDTO {
    private String date;           // 날짜
    private Long totalQuantity;     // 총 거래량
    private Long totalAmount;       // 총 거래금액
}