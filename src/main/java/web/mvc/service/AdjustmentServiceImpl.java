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
            
        if (userBuy.getState() != 6) {
            throw new RuntimeException("정산 신청 상태가 아닙니다.");
        }
        
        userBuy.setState(0); // 승인 상태로 변경
        userBuyRepository.save(userBuy);
    }
}