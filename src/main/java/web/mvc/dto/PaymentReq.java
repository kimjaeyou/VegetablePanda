package web.mvc.dto;

import lombok.Data;

// 결제 후 서버가 받아 DB에 저장할 데이터
@Data
public class PaymentReq {
    private String paymentUid;
    private String orderUid;
}
