package web.mvc.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
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
    private String file;
    private String productCategoryContent;
    private String farmerName;
    private Long shopCount;

    public ShopListDTO(Long shopSeq, Long stockSeq, String content, Integer price,
                       Integer count, String insertDate, String productName,
                       String stockGrade, String stockOrganic, String file, String productCategoryContent,
                       String farmerName) {
        this.shopSeq = shopSeq;
        this.stockSeq = stockSeq;
        this.content = content;
        this.price = price;
        this.count = count;
        this.insertDate = insertDate;
        this.productName = productName;
        this.stockGrade = stockGrade;
        this.stockOrganic = stockOrganic;
        this.file = file;
        this.productCategoryContent = productCategoryContent;
        this.farmerName = farmerName;
    }
    public ShopListDTO(Long shopSeq, Long stockSeq, String content, Integer price,
                       Integer count, String insertDate, String productName,
                       String stockGrade, String stockOrganic, String file) {
        this.shopSeq = shopSeq;
        this.stockSeq = stockSeq;
        this.content = content;
        this.price = price;
        this.count = count;
        this.insertDate = insertDate;
        this.productName = productName;
        this.stockGrade = stockGrade;
        this.stockOrganic = stockOrganic;
        this.file = file;
    }

    public ShopListDTO(Long shopSeq,String productName,String file,Integer price,Long shopCount) {
        this.shopSeq = shopSeq;
        this.price = price;
        this.productName = productName;
        this.file = file;
        this.shopCount=shopCount;
    }

    public ShopListDTO(Long shopSeq,String productName,String file,Integer price) {
        this.shopSeq = shopSeq;
        this.price = price;
        this.productName = productName;
        this.file = file;
    }

}