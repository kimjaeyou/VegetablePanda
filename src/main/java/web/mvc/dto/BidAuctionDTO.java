package web.mvc.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BidAuctionDTO {
    private Long bidSeq;
    private String productName;
    private Integer count;
    private Integer totalPrice;
    private LocalDateTime insertDate;
    private String name;
    private Integer state;

    private Long buySeq;

    public BidAuctionDTO(Long buySeq, String productName, Integer count, Integer totalPrice, LocalDateTime insertDate, String name, Integer state) {
        this.buySeq = buySeq;
        this.productName = productName;
        this.count = count;
        this.totalPrice = totalPrice;
        this.insertDate = insertDate;
        this.name = name;
        this.state = state;
    }
}
