package web.mvc.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import web.mvc.domain.UserBuy;
import web.mvc.domain.UserCharge;
import web.mvc.dto.PaymentReq;
import web.mvc.dto.RequestPayDTO;
import web.mvc.dto.UserBuyReq;

import java.util.Map;

public interface PaymentService {
    public UserBuy paymentInsert (UserBuy userBuy);

    public int saveCharge(UserCharge userCharge);

    /**
     * 마지막 충전 내역 가져오기
     */
    public UserCharge getLastCharge();

    /**
     * 결제 요청 데이터 조회
     */
    RequestPayDTO findRequestDto (String orderUid);

    /**
     * 결제 후 검증
     */
    IamportResponse<Payment> paymentByCallback(PaymentReq request);
}
