package web.mvc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AuctionStatusDTO {
    private Long auctionSeq;
    private Long stockSeq;
    private String productName;
    private String content;
    private Integer count;
    private Integer currentPrice;    // 현재 최고 입찰가
    private LocalDateTime closeTime;
    private Integer bidCount;        // 총 입찰 횟수
    private String stockGrade;       // 등급
    private String stockOrganic;     // 유기농 여부
    private Integer status;          // 경매 상태
}