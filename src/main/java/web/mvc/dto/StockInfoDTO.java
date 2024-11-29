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

    private Long productSeq;
    private String stockGrade;
    private String stockOrganic;
    private Long userSeq;
    private Long farmerUserSeq;
    private LocalDateTime regDate;

    private File file;

//    ProductDTO productDTO;
//    FarmerUserDTO farmerUserDTO;

    public StockInfoDTO (Long stockSeq, String content, Integer count, Integer color, Long productSeq, String stockGrade, String stockOrganic,
                         Long userSeq, Long farmerUserSeq, LocalDateTime regDate, File file) {
        this.stockSeq = stockSeq;
        this.content = content;
        this.count = count;
        this.color = color;
        this.productSeq = productSeq;
        this.stockGrade = stockGrade;
        this.stockOrganic = stockOrganic;
        this.userSeq = userSeq;
        this.farmerUserSeq = farmerUserSeq;
        this.regDate = regDate;
        this.file = file;
    }
}
