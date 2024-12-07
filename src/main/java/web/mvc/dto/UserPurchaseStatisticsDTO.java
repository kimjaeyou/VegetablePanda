package web.mvc.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPurchaseStatisticsDTO {
    private long auctionPurchaseCount;
    private long productPurchaseCount;
    private long companyAuctionPurchaseCount;
}
