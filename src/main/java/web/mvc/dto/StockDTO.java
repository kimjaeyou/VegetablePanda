package web.mvc.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class StockDTO {

    String stock_seq;

    String content;

    int count;

    StockGradeDTO stockGradeDTO;

    StockOrganicDTO stockOrganicDTO;

    FarmerUserDTO farmerUserDTO;

}
