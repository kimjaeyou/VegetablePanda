package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.domain.Payment;
import web.mvc.domain.UserCharge;
import web.mvc.payment.PaymentStatus;
import web.mvc.repository.PaymentRepository;
import web.mvc.repository.UserChargeRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserChargeServiceImpl implements UserChargeService {

    private final UserChargeRepository userChargeRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public UserCharge order(UserCharge userCharge) {
        // 임시 결제내역 생성
        Payment payment = Payment.builder().price(1000L).status(PaymentStatus.READY).build();
        paymentRepository.save(payment);

        // 주문 생성
        //UserCharge usercharge = UserCharge.builder().managementUser(userCharge.getManagementUser()).price(1000L).build();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd- HH:mm:ss");
        String date = format.format(new Date());
        userCharge.setChargeDate(date);
        return userChargeRepository.save(userCharge);
    }

    @Override
    public String generateOrderUid() {
        String date, randomNo;

        while(true) {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            date = format.format(new Date());
            randomNo = this.generateRandomNo(4);

            boolean result = userChargeRepository.existsByOrderUid(date + randomNo);
            if(!result) break;
        }

        return date+randomNo;
    }

    // 랜덤한 숫자 생성
    public String generateRandomNo(int length){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for(int i=0; i<length; i++){
            int ramdonNo = random.nextInt(10);
            sb.append(ramdonNo);
        }
        return sb.toString();
    }
}