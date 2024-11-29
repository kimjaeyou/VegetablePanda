package web.mvc.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBuyListByStockDTO {
    private Integer price; // 가격
    private Integer count; // 수량
    private LocalDateTime buyDate; // 구매날짜
    private String productName;
    private String productCategory;
    private String organicStatus;
    private String productGrade;

}
