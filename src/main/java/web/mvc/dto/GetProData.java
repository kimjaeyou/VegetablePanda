package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.mvc.domain.StockGrade;
import web.mvc.domain.StockOrganic;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetProData {
    private Long stockSeq;
    private String productName;
    private Integer status;
    private Integer count;
    private String stockGradeSeq;
    private String stockOrganicSeq;
    private  String path;

    public GetProData(Long stockSeq,String productName, Integer status, int count, String stockGradeSeq, String stockOrganicSeq,String path) {
        this.stockSeq = stockSeq;
        this.productName = productName;
        this.status = status;
        this.count = count;
        this.stockGradeSeq = stockGradeSeq;
        this.stockOrganicSeq = stockOrganicSeq;
        this.path = path;
    }
}
