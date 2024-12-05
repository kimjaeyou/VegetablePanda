package web.mvc.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class LikeListDTO {
    Long shopLikeSeq; // 시퀀스
    String productName; // 상품명
    LocalDateTime insertDate; // 올린 날짜
    Integer state; // 상태값

}
