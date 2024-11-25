package web.mvc.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionUserBuyDTO {
    private long userBuySeq;
    private Integer price;
    private Integer count;
}
