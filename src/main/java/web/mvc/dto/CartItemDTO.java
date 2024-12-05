package web.mvc.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CartItemDTO {
    private Long userSeq;
    private Long stockSeq;
    private Integer quantity;
    private Integer price;
    private String productName;
    private String content;
    private String imageUrl;
}