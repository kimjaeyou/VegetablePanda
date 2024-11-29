package web.mvc.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.ManagementUser;
import web.mvc.dto.*;
import web.mvc.repository.*;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class FarmerMyPageServiceImpl implements FarmerMyPageService {
    private final BuyMyPageRepository buyMyPageRepository;
    private final FarmerUserRepository farmerUserRepository;
    private final FarmerMyPageRepository farmerMyPageRepository;
    private final PasswordEncoder passwordEncoder;
    private final CalcPointRepository calcPointRepository;
    private final ReviewRepository reviewRepository;
    private final ManagementRepository managementRepository;

    @Override
    public List<UserBuyDTO> saleList(Long seq) {
        Integer state = 3; // 판매내역 조회
        return buyMyPageRepository.selectAll(seq, state);
    }

    /**
     * 회원정보 가져오기
     */
    @Override
    public FarmerUser selectUser(Long seq) {
        FarmerUser farmerUser = farmerMyPageRepository.selectUser(seq);
        return farmerUser;
    }

    /**
     * 회원정보 수정
     */
    @Modifying
    @Override
    public FarmerUser update(FarmerUser farmerUser, Long seq) {
        log.info(farmerUser.toString());
        String pw = passwordEncoder.encode(farmerUser.getPw());
        int no = farmerUserRepository.updateUser(pw, farmerUser.getAddress(), farmerUser.getPhone(), farmerUser.getEmail(), seq);

        FarmerUser farmerUser1 = farmerMyPageRepository.selectUser(seq);
        return farmerUser1;
    }

    /**
     * 회원정보 탈퇴..? 정지라고 하자
     */
    @Override
    public int delete(Long seq) {
        int i = farmerMyPageRepository.delete(seq);
        log.info("i = {}",i);
        return i;
    }

    @Override
    public List<ReviewCommentDTO2> reviewList(Long seq) {
        List<ReviewCommentDTO2> list = reviewRepository.reviewList(seq);
        log.info("list = {}",list);
        return list;
    }

    @Override
    public List<CalcPoint> calcPoint(Long seq) {
        return calcPointRepository.selectCalc(seq);
    }

    @Override
    public void settle(Long seq, List<CalcPoint> list) {
        ManagementUser managementUser = managementRepository.findSeq(seq);
        for (CalcPoint calcPoint : list) {

            web.mvc.domain.CalcPoint calcPoint1 = new web.mvc.domain.CalcPoint();
            calcPoint1.setManagementUser(managementUser);
            calcPoint1.setTotalPoint(calcPoint.getTotalPoint());
            calcPoint1.setInsertDate(calcPoint.getInsertDate());
            calcPoint1.setState(calcPoint.getState());

            log.info("정산 신청 성공? ={}",calcPoint1);

            calcPointRepository.save(calcPoint1);
        }
    }
}