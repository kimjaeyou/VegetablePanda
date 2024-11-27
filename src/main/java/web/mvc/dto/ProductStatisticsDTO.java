package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatisticsDTO {
    private String productName;  // 상품명
    private Long totalQuantity;  // 총 판매수량
    private Long totalAmount;    // 총 판매금액
    private Long orderCount;     // 주문 횟수
}