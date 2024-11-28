package web.mvc.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
public class BidAuctionDTO {
    private Long bidSeq;
    private String content;
    private Integer count;
    private Integer price;
    private LocalDateTime insertDate;
    private String name;
    private Integer status;

    public BidAuctionDTO(Long bidSeq, String content, Integer count, Integer price, LocalDateTime insertDate, String name, Integer status) {
        this.bidSeq = bidSeq;
        this.content = content;
        this.count = count;
        this.price = price;
        this.insertDate = insertDate;
        this.name = name;
        this.status = status;
    }
}
