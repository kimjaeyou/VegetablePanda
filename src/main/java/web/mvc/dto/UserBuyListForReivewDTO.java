package web.mvc.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserBuyListForReivewDTO {
    private Long userBuyDetailSeq;
    private String content;
    private Integer count;
    private Integer price;
    private LocalDateTime buyDate;
    private Integer state;
    private Long reviewSeq;


}
