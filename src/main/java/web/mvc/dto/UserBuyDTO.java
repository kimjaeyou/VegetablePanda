package web.mvc.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString
public class UserBuyDTO {
    private Long userBuySeq;
    private String content;
    private Integer count;
    private Integer price;
    private LocalDateTime buyDate;
    private Integer state;

    public UserBuyDTO(Long userBuySeq, String content, Integer count, Integer price, LocalDateTime buyDate, Integer state) {
        this.userBuySeq = userBuySeq;
        this.content = content;
        this.count = count;
        this.price = price;
        this.buyDate = buyDate;
        this.state = state;
    }

    public UserBuyDTO(String content, Integer count, Integer price, LocalDateTime buyDate, Integer state) {
        this.content = content;
        this.count = count;
        this.price = price;
        this.buyDate = buyDate;
        this.state = state;
    }
}
