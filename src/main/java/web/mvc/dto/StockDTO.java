package web.mvc.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO {

    private Long stockSeq;
    private String content;
    private int count;
    private int color;
    private Long productSeq;
    private Long stockGradeSeq;
    private Long stockOrganicSeq;
    private Long userSeq;
    private Long farmerUserSeq;
    private LocalDateTime regDate;
    private FileDTO file;

//    ProductDTO productDTO;
//
//    StockGradeDTO stockGradeDTO;
//
//    StockOrganicDTO stockOrganicDTO;
//
//    FarmerUserDTO farmerUserDTO;

}
