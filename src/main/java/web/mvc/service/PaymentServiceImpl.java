package web.mvc.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.*;
import web.mvc.dto.PaymentReq;
import web.mvc.dto.RequestPayDTO;
import web.mvc.exception.*;
import web.mvc.payment.PaymentStatus;
import web.mvc.repository.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final UserChargeRepository userChargeRepository;
    private final CompanyUserRepository companyUserRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final UserBuyDetailRepository userBuyDetailRepository;

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
    public RequestPayDTO findRequestDto(String orderUid, int status) {
        ManagementUser userM = null;
        RequestPayDTO requestPayDTO = null;

        switch(status){
            case 1: // 포인트 충전
                List<UserCharge> userCharge = userChargeRepository.findByOrderUid(orderUid);
                log.info("findRequestDto에서 검색한 userChrage : {}", userCharge);
                userM = userCharge.get(0).getManagementUser();

                if("user".equals(userM.getContent())){

                    List<User> user = userRepository.findListByUserSeq(userM.getUserSeq());
                    requestPayDTO = RequestPayDTO.builder().buyerName(user.get(0).getName()).buyerEmail(user.get(0).getEmail()).buyerAddr(user.get(0).getAddress())
                            .itemName("포인트").paymentPrice(userCharge.get(0).getPrice()).orderUid(orderUid).build();

                } else if ("company".equals(userM.getContent())){

                    CompanyUser user = companyUserRepository.findByUserSeq(userM.getUserSeq());
                    requestPayDTO = RequestPayDTO.builder().buyerName(user.getComName()).buyerEmail(user.getEmail()).buyerAddr(user.getAddress())
                            .itemName("포인트").paymentPrice(userCharge.get(0).getPrice()).orderUid(orderUid).build();

                } else {
                    throw new MemberAuthenticationException(ErrorCode.NOTFOUND_USER);
                }

                break;

            case 2: // 일반 결제 요청
                UserBuy userBuy = userBuyRepository.findById(Long.parseLong(orderUid)).orElseThrow(()->new UserChargeException(ErrorCode.NOTFOUND_USER));
                log.info("일반 결제 요청 userBuy : {}", userBuy);
                userM = userBuy.getManagementUser();
                log.info("일반 결제 요청 userM : {}", userM);

                if("user".equals(userM.getContent())){

                    //List<User> user = userRepository.findListByUserSeq(userM.getUserSeq());
                    User user = userRepository.findByUserSeq(userM.getUserSeq());
//                    requestPayDTO = RequestPayDTO.builder().buyerName(user.get(0).getName()).buyerEmail(user.get(0).getEmail()).buyerAddr(user.get(0).getAddress())
//                            .itemName(userBuyDetailRepository.findByBuySeq(userBuy.getBuySeq())).paymentPrice((long)(userBuy.getTotalPrice())).orderUid(orderUid).build();
                    List<String> items = userBuyDetailRepository.findByBuySeq(userBuy.getBuySeq());
                    requestPayDTO = RequestPayDTO.builder().buyerName(user.getName()).buyerEmail(user.getEmail()).buyerAddr(user.getAddress())
                            .itemName(items.get(0)).paymentPrice((long)(userBuy.getTotalPrice())).orderUid(orderUid).build();

                } else if ("company".equals(userM.getContent())){ // 업체 사용자 일반 물품 구매 안됐던거같은데

                    throw new MemberAuthenticationException(ErrorCode.ORDER_FORBIDDEN);

                } else {
                    throw new MemberAuthenticationException(ErrorCode.NOTFOUND_USER);
                }

                break;
        }

        log.info("findRequestDto userM : {}", userM);
        //User user;
        //ManagementUser user = managementRepository.findById(userCharge.getManagementUser().getUserSeq()).orElseThrow(()-> new MemberAuthenticationException(ErrorCode.NOTFOUND_USER));

        // ManagementUser에서 각 사용자 꺼내기
        // 아니면 쿼리문 사용 (select m.user_seq, u.name, c.com_name, f.name, u.email, c.email, f.email, u.address, c.address, f.address from management_user m left join user u on m.user_seq = u.user_seq left join company_user c on m.user_seq = c.user_seq left join farmer_user f on m.user_seq = f.user_seq;)

        return requestPayDTO;
    }

    @Override
    public IamportResponse<Payment> paymentByChargeCallback(PaymentReq request) {
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

            // 검증 후 포인트 충전
            chargeWallet(price, userCharge.getManagementUser().getUserSeq());

            // 결제 상태 OK로 변경
            userCharge.getPayment().changePaymentBySuccess(PaymentStatus.OK, iamportResponse.getResponse().getImpUid());
            return iamportResponse;

        } catch (IamportResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public IamportResponse<Payment> paymentByCallback(PaymentReq request) {
        try {
            // 결제 단건 조회
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getPaymentUid());
            // 주문내역 조회
            //UserCharge userCharge = userChargeRepository.findByOrderUid(request.getOrderUid()).get(0);
            UserBuy userBuy = userBuyRepository.findOrderAndPayment(Long.parseLong(request.getOrderUid())).orElseThrow(()-> new UserChargeException(ErrorCode.ORDER_NOTFOUND));
            log.info("userBuy : {}", userBuy);

            // 결제가 완료되지 않은 상태인 경우
            if(!iamportResponse.getResponse().getStatus().equals("paid")){
                // 충전, 결제 정보 삭제
                paymentRepository.delete(userBuy.getPayment());
                userBuyRepository.delete(userBuy);

                throw new UserChargeException(ErrorCode.ORDER_NOTPAID);
            }

            // DB에 저장된 결제 금액
            Long price = (long)userBuy.getTotalPrice();
            // 실 결제 금액
            long iamportPrice = iamportResponse.getResponse().getAmount().longValue();

            // 결제 금액 검증
            if(price != iamportPrice) {
                // 충전, 결제 정보 삭제
                userBuyRepository.delete(userBuy);
                paymentRepository.delete(userBuy.getPayment());

                // 위변조 의심되는 결제 취소
                iamportClient.cancelPaymentByImpUid(new CancelData(iamportResponse.getResponse().getImpUid(), true, new BigDecimal(iamportPrice)));

                throw new UserChargeException(ErrorCode.ORDER_CANCELED);
            }

            // 검증 후 포인트 충전
            // chargeWallet(price, userCharge.getManagementUser().getUserSeq());

            // 검증 후 주문상태 변경
            UserBuy result = changeOrder(Long.parseLong(request.getOrderUid()));

            // 결제 상태 OK로 변경
            userBuy.getPayment().changePaymentBySuccess(PaymentStatus.OK, iamportResponse.getResponse().getImpUid());
            return iamportResponse;

        } catch (IamportResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 유저 지갑에 포인트 충전
    public UserWallet chargeWallet(long point, long userSeq) {
        UserWallet wallet = walletRepository.findByUserSeq(userSeq);
        int dbPoint = wallet.getPoint();
        int finalPoint = dbPoint + (int)point;
        wallet.setPoint(finalPoint);
        return wallet;
    }

    // 주문상태 변경
    public UserBuy changeOrder(long buySeq) {
        UserBuy userBuy = userBuyRepository.findById(buySeq).orElseThrow(()-> new UserBuyException(ErrorCode.ORDER_NOTFOUND));
        userBuy.setState(2);
        return userBuy;
    }

    // 포인트 충전을 위한 결제 요청 객체 검색 메소드
    public List<UserCharge> findUserChargeByOrderUid(String orderUid) {
        List<UserCharge> userCharge = userChargeRepository.findByOrderUid(orderUid);
        log.info("userCharge : {}", userCharge);
        return userCharge;
    }

    // 일반 결제를 위한 결제 요청 객체 검색 메소드
    public UserBuy findUserBuyByOrderUid(String orderUid) {
        UserBuy userBuy = userBuyRepository.findById(Long.parseLong(orderUid)).orElseThrow(()->new UserChargeException(ErrorCode.NOTFOUND_USER));
        log.info("userBuy : {}", userBuy);
        return userBuy;
    }

}
