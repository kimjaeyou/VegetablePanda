package web.mvc.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import web.mvc.domain.*;
import web.mvc.dto.PaymentReq;
import web.mvc.dto.RequestPayDTO;
import web.mvc.exception.ErrorCode;
import web.mvc.exception.MemberAuthenticationException;
import web.mvc.exception.PaymentException;
import web.mvc.repository.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final UserChargeRepository userChargeRepository;
    private final ManagementRepository managementRepository;
    private final FarmerUserRepository farmerUserRepository;
    private final CompanyUserRepository companyUserRepository;
    private final UserRepository userRepository;

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
        UserCharge userCharge = userChargeRepository.findUserChargeAndPaymentAndManagementUser(orderUid).orElseThrow(()-> new PaymentException(ErrorCode.ORDER_NOTFOUND));
        ManagementUser userM = userCharge.getManagementUser();
        RequestPayDTO requestPayDTO;
        //User user;
        //ManagementUser user = managementRepository.findById(userCharge.getManagementUser().getUserSeq()).orElseThrow(()-> new MemberAuthenticationException(ErrorCode.NOTFOUND_USER));

        // ManagementUser에서 각 사용자 꺼내기
        // 아니면 쿼리문 사용 (select m.user_seq, u.name, c.com_name, f.name, u.email, c.email, f.email, u.address, c.address, f.address from management_user m left join user u on m.user_seq = u.user_seq left join company_user c on m.user_seq = c.user_seq left join farmer_user f on m.user_seq = f.user_seq;)
        if("user".equals(userM.getContent())){
            User user = userRepository.find(userM.getUserSeq());
            requestPayDTO = RequestPayDTO.builder().buyerName(user.getName()).buyerEmail(user.getEmail()).buyerAddr(user.getAddress())
                    .itemName("포인트").paymentPrice(userCharge.getPrice()).orderUid(orderUid).build();
        } else if ("company".equals(userM.getContent())){
            CompanyUser user = companyUserRepository.findByUserSeq(userM.getUserSeq());
            requestPayDTO = RequestPayDTO.builder().buyerName(user.getComName()).buyerEmail(user.getEmail()).buyerAddr(user.getAddress())
                    .itemName("포인트").paymentPrice(userCharge.getPrice()).orderUid(orderUid).build();
        } else {
            throw new MemberAuthenticationException(ErrorCode.NOTFOUND_USER);
        }

        return requestPayDTO;
    }

    @Override
    public IamportResponse<Payment> paymentByCallback(PaymentReq request) {
        return null;
    }
}
