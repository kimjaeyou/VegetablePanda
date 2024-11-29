package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import web.mvc.domain.UserBuy;
import web.mvc.dto.UserBuyListByStockDTO;
import web.mvc.repository.UserBuyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBuyServiceImpl implements UserBuyService {
    private final UserBuyRepository userBuyRepository;


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
}
