package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShopListDTO {
    private Long shopSeq;
    private Long stockSeq;
    private String content;
    private Integer price;
    private Integer count;
    private String insertDate;
    private String productName;
    private String stockGrade;
    private String stockOrganic;

    public ShopListDTO(Long shopSeq, Long stockSeq, String content, Integer price,
                       Integer count, String insertDate, String productName,
                       String stockGrade, String stockOrganic) {
        this.shopSeq = shopSeq;
        this.stockSeq = stockSeq;
        this.content = content;
        this.price = price;
        this.count = count;
        this.insertDate = insertDate;
        this.productName = productName;
        this.stockGrade = stockGrade;
        this.stockOrganic = stockOrganic;
    }
}