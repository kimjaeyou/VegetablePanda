package web.mvc.dto;

import lombok.*;

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
    private Long user_seq;
//    ProductDTO productDTO;
//
//    StockGradeDTO stockGradeDTO;
//
//    StockOrganicDTO stockOrganicDTO;
//
//    FarmerUserDTO farmerUserDTO;

}
