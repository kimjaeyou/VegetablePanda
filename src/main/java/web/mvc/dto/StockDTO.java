package web.mvc.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO {

    private int stockSeq;
    private String content;
    private int count;
    private String color;

    private int productSeq;
    private String stockGradeSeq;
    private String stockOrganicSeq;
    private long farmerUserSeq;
//    ProductDTO productDTO;
//
//    StockGradeDTO stockGradeDTO;
//
//    StockOrganicDTO stockOrganicDTO;
//
//    FarmerUserDTO farmerUserDTO;

}
