package web.mvc.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import web.mvc.domain.Payment;
import web.mvc.domain.UserBuy;
import web.mvc.domain.UserCharge;
import web.mvc.dto.PaymentReq;
import web.mvc.dto.RequestPayDTO;
import web.mvc.repository.UserChargeRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final UserChargeRepository userChargeRepository;
    private final IamportClient iamportClient;

    @Override
    public UserBuy paymentInsert(UserBuy userBuy) {
        // 결제 정보 등록 프로세스 // 주문에서 진행가능...
        return null;
    }

    @Override
    public int saveCharge(UserCharge userCharge) {
        log.info("포인트 충전 성공! 충전 포인트 : {}", userCharge.getPrice());
        UserCharge result = userChargeRepository.save(userCharge);
        return 1;
    }

    @Override
    public UserCharge getLastCharge() {
        return userChargeRepository.getLastCharge();
    }

    @Override
    public RequestPayDTO findRequestDto(String orderUid) {


        return null;
    }

    @Override
    public IamportResponse<Payment> paymentByCallback(PaymentReq request) {
        return null;
    }
}
