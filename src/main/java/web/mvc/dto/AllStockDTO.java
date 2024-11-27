package web.mvc.dto;

import lombok.*;
import web.mvc.domain.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllStockDTO {

    private String content;
    private Integer count;
    private Integer color;
    private String productName;
    private String stockGrade;
    private String stockOrganic;
    private String farmerUserName;
    private String farmerUserGrade;
    private String farmerUserPhone;
    private String filePath;



}
