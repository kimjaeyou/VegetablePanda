package web.mvc.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserBuyDetailDTO {
    private Long userBuySeq;
    private Long buySeq; // UserBuy의 ID (예시)
    private Integer price;
    private Integer count;
    private Long stockSeq; // stock_seq를 사용
}