package web.mvc.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class StockDTO {

    int stockSeq;

    String content;

    int count;

    ProductDTO productDTO;

    StockGradeDTO stockGradeDTO;

    StockOrganicDTO stockOrganicDTO;

    FarmerUserDTO farmerUserDTO;

}
