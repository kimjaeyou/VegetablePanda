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
    private Integer count;
    private Integer price;
    private String file;
    private Long stockSeq;

    public UserBuyDetailInfoDTO(String productName, Integer count, Integer price, String file, Long stockSeq) {
        this.productName = productName;
        this.count = count;
        this.price = price;
        this.file = file;
        this.stockSeq = stockSeq;
    }

}
