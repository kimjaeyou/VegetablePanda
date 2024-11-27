package web.mvc.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web.mvc.domain.CalcPoint;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.ManagementUser;
import web.mvc.dto.FarmerUserDTO;
import web.mvc.dto.UserBuyDTO;
import web.mvc.repository.*;

import java.sql.Date;
import java.time.LocalDate;
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

    @Override
    public List<UserBuyDTO> buyList(Long seq) {
        return buyMyPageRepository.saleSelectAll(seq);
    }

    /**
     * 회원정보 가져오기
     */
    @Override
    public FarmerUser selectUser(Long seq) {
        FarmerUser farmerUser = farmerMyPageRepository.selectUser(seq);
        System.out.println("farmerUser: " + farmerUser.toString());
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
        log.info("no={}", no);

        FarmerUser farmerUser1 = farmerMyPageRepository.selectUser(seq);
        log.info("회원 수정 성공~");
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
    public void calcPoint(Long seq, UserBuyDTO userBuyDTO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        CalcPoint calcPoint = new CalcPoint(
                new ManagementUser(seq), // 유저 시퀀스
                userBuyDTO.getPrice(), // 금액
                localDateTime,// 현재 신청 날짜
                1); // 신청중으로 상태값 변경
        calcPointRepository.save(calcPoint);
        log.info("calcPoint = {}", calcPoint);
    }


}