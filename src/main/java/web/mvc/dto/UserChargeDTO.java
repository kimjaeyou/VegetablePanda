package web.mvc.dto;

import lombok.Getter;
import lombok.Setter;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.UserCharge;

@Getter
@Setter
public class UserChargeDTO {
    private int userChargeSeq;
    private int managementUserSeq;
    private String chargeDate;
    private long price;
    private String orderUid;

    public UserCharge toUserCharge(UserChargeDTO userChargeDTO) {
        return UserCharge.builder()
                .managementUser(new ManagementUser(userChargeDTO.getManagementUserSeq())).price(userChargeDTO.getPrice())
                .build();
    }
}
