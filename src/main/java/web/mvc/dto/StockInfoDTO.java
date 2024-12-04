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

    private String productCategoryContent;
    private Long productSeq;
    private String productName;
    private String stockGrade;
    private String stockOrganic;
    private Long userSeq;
    private Long farmerUserSeq;
    private LocalDateTime regDate;

    private Long fileSeq;
    private String filePath;
    private String fileName;

//    private FileDTO fileDTO;
//    private File file;

//    ProductDTO productDTO;
//    FarmerUserDTO farmerUserDTO;

    public StockInfoDTO (Long stockSeq, String content, Integer count, Integer color, String productCategoryContent, Long productSeq, String productName, String stockGrade, String stockOrganic,
                         Long userSeq, Long farmerUserSeq, LocalDateTime regDate, Long fileSeq, String fileName, String filePath) {
        this.stockSeq = stockSeq;
        this.content = content;
        this.count = count;
        this.color = color;
        this.productCategoryContent = productCategoryContent;
        this.productSeq = productSeq;
        this.productName = productName;
        this.stockGrade = stockGrade;
        this.stockOrganic = stockOrganic;
        this.userSeq = userSeq;
        this.farmerUserSeq = farmerUserSeq;
        this.regDate = regDate;
        this.fileSeq = fileSeq;
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
