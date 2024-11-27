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
    private String color;

    private Long productSeq;
    private String stockGradeSeq;
    private String stockOrganicSeq;
    private Long farmerUserSeq;
    private LocalDateTime regDate;
//    ProductDTO productDTO;
//
//    StockGradeDTO stockGradeDTO;
//
//    StockOrganicDTO stockOrganicDTO;
//
//    FarmerUserDTO farmerUserDTO;

}
