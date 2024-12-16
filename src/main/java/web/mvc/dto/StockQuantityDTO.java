package web.mvc.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StockQuantityDTO {
    private long stockSeq;
    private int quantity;
}
