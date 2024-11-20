package web.mvc.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO {

    int stockSeq;

    String content;

    int count;

    int productSeq;

    String stockGradeSeq;

    String stockOrganicSeq;

    long farmerUserSeq;

    int price;

}
