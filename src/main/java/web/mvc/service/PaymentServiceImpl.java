package web.mvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import web.mvc.domain.UserBuy;
import web.mvc.dto.UserBuyReq;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    @Override
    public UserBuy paymentInsert(UserBuyReq userBuyReq) {
        // 결제 정보 등록 프로세스
        return null;
    }
}
