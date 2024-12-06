package web.mvc.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.Stock;
import web.mvc.domain.UserBuy;
import web.mvc.dto.*;
import web.mvc.repository.*;

import java.time.LocalDateTime;
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
    private final S3ImageService s3ImageService;
    private final FileRepository fileRepository;
    private final StockRepository stockRepository;

    @Override
    public List<UserBuyDTO> saleList(Long seq) {
        return buyMyPageRepository.selectAll(seq);
    }

    /**=2
     * 회원정보 가져오기
     */
    @Override
    public FarmerUserDTO2 selectUser(Long seq) {
        return farmerMyPageRepository.selectUser(seq);
    }

    /**
     * 회원정보 수정
     */
    @Modifying
    @Override
    public FarmerUser update(GetAllUserDTO getAllUserDTO, Long seq) {
        String pw = passwordEncoder.encode(getAllUserDTO.getPw());
        String name = getAllUserDTO.getName();
        String email = getAllUserDTO.getEmail();
        String code = getAllUserDTO.getCode();
        String address = getAllUserDTO.getAddress();
        String phone = getAllUserDTO.getPhone();

        farmerUserRepository.updateUser( name, email, code, address, phone, pw, seq);

        return farmerUserRepository.findByUserSeq(seq);
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
    @Transactional
    public void settle(Long seq, List<CalcPoint2> list) {
        ManagementUser managementUser = managementRepository.findSeq(seq);
        LocalDateTime now = LocalDateTime.now();

        for (CalcPoint2 calcPoint : list) {
            // 현재 상태 확인
            Long buySeq = calcPoint.getUserBuySeq();
            UserBuy userBuy = buyMyPageRepository.findById(buySeq)
                    .orElseThrow(() -> new RuntimeException("구매 정보를 찾을 수 없습니다."));

            log.info("정산 처리 전 - buySeq: {}, 현재 상태: {}", buySeq, userBuy.getState());

            // 상태값 업데이트
            int updateResult = buyMyPageRepository.update(buySeq);
            log.info("상태값 업데이트 결과: {}", updateResult);

            if (updateResult > 0) {
                // 정산 포인트 저장
                web.mvc.domain.CalcPoint calcPoint1 = new web.mvc.domain.CalcPoint();
                calcPoint1.setManagementUser(managementUser);
                calcPoint1.setTotalPoint(calcPoint.getTotalPoint());
                calcPoint1.setInsertDate(now);
                calcPoint1.setState(1);
                calcPointRepository.save(calcPoint1);

                // 업데이트 후 상태 확인
                UserBuy updatedUserBuy = buyMyPageRepository.findById(buySeq)
                        .orElseThrow(() -> new RuntimeException("구매 정보를 찾을 수 없습니다."));
                log.info("정산 처리 후 - buySeq: {}, 변경된 상태: {}", buySeq, updatedUserBuy.getState());
            } else {
                log.error("상태값 업데이트 실패 - buySeq: {}", buySeq);
            }
        }
    }



}
