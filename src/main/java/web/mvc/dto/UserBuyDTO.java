package web.mvc.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBuyDTO {
    private long userBuySeq;
    private String content;
    private Integer price;
    private Integer count;
}
