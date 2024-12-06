package web.mvc.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopLikeResponseDTO {
    private Long shopSeq;
    private Long stockSeq;
    private String productName;
    private String productCategoryContent;
    private Integer price;
    private String imagePath;
    private String content;
    private Integer count;
    private String stockGrade;
    private String stockOrganic;
    private String file;
}
