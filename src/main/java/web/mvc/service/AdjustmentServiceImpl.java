package web.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.UserBuy;
import web.mvc.dto.AdjustmentDTO;
import web.mvc.repository.UserBuyRepository;

import java.util.List;

@Service
@Transactional
public class AdjustmentServiceImpl implements AdjustmentService {
    
    @Autowired
    private UserBuyRepository userBuyRepository;

    @Override
    public List<AdjustmentDTO> getPendingSettlements() {
        return userBuyRepository.findPendingSettlements();
    }

    @Override
    public void approveSettlement(Long buySeq) {
        UserBuy userBuy = userBuyRepository.findById(buySeq)
                .orElseThrow(() -> new RuntimeException("해당 주문을 찾을 수 없습니다."));

        // 상태값에 따른 처리
        switch (userBuy.getState()) {
            case 6:
                userBuy.setState(10); // 6 -> 10
                break;
            case 7:
                userBuy.setState(9);  // 7 -> 9
                break;
            case 8:
                userBuy.setState(0);  // 8 -> 0
                break;
            default:
                throw new RuntimeException("올바른 정산 신청 상태가 아닙니다.");
        }

        userBuyRepository.save(userBuy);
    }
}