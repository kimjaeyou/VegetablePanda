package web.mvc.dto;

import lombok.*;
import web.mvc.domain.File;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class StockInfoDTO {
    private Long stockSeq;
    private String content;
    private int count;
    private int color;

    private String productCategoryName;
    private Long productSeq;
    private String productName;
    private String stockGrade;
    private String stockOrganic;
    private Long userSeq;
    private Long farmerUserSeq;
    private LocalDateTime regDate;

    private FileDTO fileDTO;

//    ProductDTO productDTO;
//    FarmerUserDTO farmerUserDTO;

    public StockInfoDTO (Long stockSeq, String content, Integer count, Integer color, String productCategoryName, Long productSeq, String productName, String stockGrade, String stockOrganic,
                         Long userSeq, Long farmerUserSeq, LocalDateTime regDate, File file) {
        this.stockSeq = stockSeq;
        this.content = content;
        this.count = count;
        this.color = color;
        this.productCategoryName = productCategoryName;
        this.productSeq = productSeq;
        this.productName = productName;
        this.stockGrade = stockGrade;
        this.stockOrganic = stockOrganic;
        this.userSeq = userSeq;
        this.farmerUserSeq = farmerUserSeq;
        this.regDate = regDate;
        this.fileDTO = new FileDTO(file);
    }
}
