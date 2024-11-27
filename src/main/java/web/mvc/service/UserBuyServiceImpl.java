package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Payment;
import web.mvc.domain.UserBuy;
import web.mvc.payment.PaymentStatus;
import web.mvc.repository.PaymentRepository;
import web.mvc.repository.UserBuyRepository;
import web.mvc.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@DynamicUpdate
public class UserBuyServiceImpl implements UserBuyService {

    private final UserBuyRepository userBuyRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public UserBuy buy(UserBuy userBuy) {
        /*

        */
        return null;
    }

    @Override
    public UserBuy insertShopOrder(UserBuy userBuy) {

        // 임시 주문내역 생성
        UserBuy result = userBuyRepository.save(userBuy);

        // 임시 결제내역 생성
        Payment payment = Payment.builder().price(userBuy.getTotalPrice()).status(PaymentStatus.READY)//.usercharge(UserCharge.builder().userChargeSeq())
                .userBuy(UserBuy.builder().buySeq(result.getBuySeq()).build())
                .build();
        paymentRepository.save(payment);

        userBuy.setPayment(payment);

        // 주문 생성
        //UserCharge usercharge = UserCharge.builder().managementUser(userCharge.getManagementUser()).price(1000L).build();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd- HH:mm:ss");
//        String date = format.format(new Date());
//        LocalDateTime dateTime = LocalDateTime.parse(date);
//        userBuy.setBuyDate(dateTime);

        return result;
    }
}
