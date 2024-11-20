package web.mvc.dto;

import lombok.Builder;
import lombok.Data;

// View로 전달할 결제 관련 데이터
@Data
public class RequestPayDTO {
    private String orderUid;
    private String itemName;
    private String buyerName;
    private Long paymentPrice;
    private String buyerEmail;
    private String buyerAddr;

    @Builder
    public RequestPayDTO(String orderUid, String itemName, String buyerName, Long paymentPrice, String buyerEmail, String buyerAddr) {
        this.orderUid = orderUid;
        this.itemName = itemName;
        this.buyerName = buyerName;
        this.paymentPrice = paymentPrice;
        this.buyerEmail = buyerEmail;
        this.buyerAddr = buyerAddr;
    }
}
