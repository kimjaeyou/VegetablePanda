package web.mvc.dto;

import lombok.*;
import web.mvc.domain.UserBuy;

@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString
public class UserBuyDetailInfoDTO {
    private String productName;
    private Integer quantity;
    private Integer price;
    private String imageUrl;
    private Long stockSeq;

    public UserBuyDetailInfoDTO(String productName, Integer quantity, Integer price, String imageUrl, Long stockSeq) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stockSeq = stockSeq;
    }

}
