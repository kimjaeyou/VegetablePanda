package web.mvc.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Service;
import web.mvc.domain.Payment;
import web.mvc.domain.UserBuy;
import web.mvc.domain.UserBuyDetail;
import web.mvc.dto.UserBuyListByStockDTO;
import web.mvc.repository.*;
import web.mvc.exception.ErrorCode;
import web.mvc.exception.UserBuyException;
import web.mvc.payment.PaymentStatus;
import web.mvc.repository.UserBuyRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@DynamicUpdate
public class UserBuyServiceImpl implements UserBuyService {
    private final UserBuyRepository userBuyRepository;

    private final PaymentRepository paymentRepository;
    private final UserBuyDetailRepository userBuyDetailRepository;

    @Override
    public UserBuy buy(UserBuy userBuy) {
        /*

        */
        return null;
    }

    @Override
    public List<UserBuyListByStockDTO> geUserBuyListByStockDtos(Long stockSeq) {
        return userBuyRepository.findByStockSeq(stockSeq);
    }

    @Override
    public UserBuy insertShopOrder(UserBuy userBuy) {

        // 임시 주문내역 생성
        UserBuy result = userBuyRepository.save(userBuy);

        // 임시 결제내역 생성
        Payment payment = Payment.builder().price(userBuy.getTotalPrice()).status(PaymentStatus.READY)//.usercharge(UserCharge.builder().userChargeSeq())
                //.userBuy(UserBuy.builder().buySeq(result.getBuySeq()).build())
                .build();
        paymentRepository.save(payment);

        userBuy.setPayment(payment);
        return result;
    }

    @Override
    public int deleteOrder(long userBuySeq) {
        UserBuy userBuy = userBuyRepository.findById(userBuySeq).orElseThrow(()-> new UserBuyException(ErrorCode.ORDER_NOTFOUND));
        userBuyRepository.delete(userBuy);
        return 1;
    }

    @Override
    public Optional<UserBuyDetail> findById(Long id) {
        return userBuyDetailRepository.findById(id);
    }
}
