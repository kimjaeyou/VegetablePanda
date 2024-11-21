package web.mvc.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import web.mvc.domain.*;
import web.mvc.dto.PaymentReq;
import web.mvc.dto.RequestPayDTO;
import web.mvc.exception.ErrorCode;
import web.mvc.exception.MemberAuthenticationException;
import web.mvc.exception.PaymentException;
import web.mvc.exception.UserChargeException;
import web.mvc.payment.PaymentStatus;
import web.mvc.repository.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final UserChargeRepository userChargeRepository;
    private final ManagementRepository managementRepository;
    private final FarmerUserRepository farmerUserRepository;
    private final CompanyUserRepository companyUserRepository;
    private final UserRepository userRepository;
    private final UserBuyRepository buyRepository;

    private final IamportClient iamportClient;
    private final PaymentRepository paymentRepository;
    private final UserBuyRepository userBuyRepository;

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
    public RequestPayDTO findRequestDto(int status, String orderUid) {
        //UserCharge userCharge = userChargeRepository.findUserChargeAndPaymentAndManagementUser(orderUid).orElseThrow(()-> new PaymentException(ErrorCode.ORDER_NOTFOUND));
        ManagementUser userM = null;
        List<UserCharge> userCharge = userChargeRepository.findByOrderUid(orderUid);
        log.info("userChage : {}", userCharge);
        userM = userCharge.get(0).getManagementUser();

        switch(status){
            case 1: // 포인트 충전
                userCharge = userChargeRepository.findByOrderUid(orderUid);
                log.info("userChage : {}", userCharge);
                userM = userCharge.get(0).getManagementUser();

                break;
            case 2: // 일반 결제 요청
                UserBuy userBuy = userBuyRepository.findById(Long.parseLong(orderUid)).orElseThrow(()->new UserChargeException(ErrorCode.NOTFOUND_USER));
                System.out.println(userBuy);
                log.info("userBuy : {}", userBuy);
                userM = userBuy.getManagementUser();

                break;
        }

        RequestPayDTO requestPayDTO;
        log.info("findRequestDto userM : {}", userM);
        //User user;
        //ManagementUser user = managementRepository.findById(userCharge.getManagementUser().getUserSeq()).orElseThrow(()-> new MemberAuthenticationException(ErrorCode.NOTFOUND_USER));

        // ManagementUser에서 각 사용자 꺼내기
        // 아니면 쿼리문 사용 (select m.user_seq, u.name, c.com_name, f.name, u.email, c.email, f.email, u.address, c.address, f.address from management_user m left join user u on m.user_seq = u.user_seq left join company_user c on m.user_seq = c.user_seq left join farmer_user f on m.user_seq = f.user_seq;)
        if("user".equals(userM.getContent())){
            List<User> user = userRepository.findListByUserSeq(userM.getUserSeq());
            requestPayDTO = RequestPayDTO.builder().buyerName(user.get(0).getName()).buyerEmail(user.get(0).getEmail()).buyerAddr(user.get(0).getAddress())
                    .itemName("포인트").paymentPrice(userCharge.get(0).getPrice()).orderUid(orderUid).build();
        } else if ("company".equals(userM.getContent())){
            CompanyUser user = companyUserRepository.findByUserSeq(userM.getUserSeq()).get(0);
            requestPayDTO = RequestPayDTO.builder().buyerName(user.getComName()).buyerEmail(user.getEmail()).buyerAddr(user.getAddress())
                    .itemName("포인트").paymentPrice(userCharge.get(0).getPrice()).orderUid(orderUid).build();
        } else {
            throw new MemberAuthenticationException(ErrorCode.NOTFOUND_USER);
        }

        return requestPayDTO;
    }

    @Override
    public IamportResponse<Payment> paymentByCallback(PaymentReq request) {
        try {
            // 결제 단건 조회
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getPaymentUid());
            // 주문내역 조회
            //UserCharge userCharge = userChargeRepository.findByOrderUid(request.getOrderUid()).get(0);
            UserCharge userCharge = userChargeRepository.findOrderAndPayment(request.getOrderUid()).orElseThrow(()-> new UserChargeException(ErrorCode.ORDER_NOTFOUND));
            log.info("usercharge : {}", userCharge);

            // 결제가 완료되지 않은 상태인 경우
            if(!iamportResponse.getResponse().getStatus().equals("paid")){
                // 충전, 결제 정보 삭제
                userChargeRepository.delete(userCharge);
                paymentRepository.delete(userCharge.getPayment());

                throw new UserChargeException(ErrorCode.ORDER_NOTPAID);
            }

            // DB에 저장된 결제 금액
            Long price = userCharge.getPayment().getPrice();
            // 실 결제 금액
            long iamportPrice = iamportResponse.getResponse().getAmount().longValue();

            // 결제 금액 검증
            if(price != iamportPrice) {
                // 충전, 결제 정보 삭제
                userChargeRepository.delete(userCharge);
                paymentRepository.delete(userCharge.getPayment());

                // 위변조 의심되는 결제 취소
                iamportClient.cancelPaymentByImpUid(new CancelData(iamportResponse.getResponse().getImpUid(), true, new BigDecimal(iamportPrice)));

                throw new UserChargeException(ErrorCode.ORDER_CANCELED);
            }

            // 결제 상태 OK로 변경
            userCharge.getPayment().changePaymentBySuccess(PaymentStatus.OK, iamportResponse.getResponse().getImpUid());
            return iamportResponse;

        } catch (IamportResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
