package web.mvc.dto;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdjustmentDTO {
    private Long buySeq;
    private String farmerName;
    private String productName;
    private Integer totalPrice;
    private Integer state;
    private LocalDateTime buyDate;
}
