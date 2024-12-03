package web.mvc.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    private Long stockSeq;
    private Integer quantity;
    private Integer price;
    private String productName;
    private String content;
    private String imageUrl;
}