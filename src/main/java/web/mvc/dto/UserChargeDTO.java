package web.mvc.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChargeDTO {
    private int userChargeSeq;
    private int managementUserSeq;
    private String chargeDate;
    private int price;
}
