package web.mvc.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web.mvc.domain.FarmerUser;
import web.mvc.repository.*;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class FarmerMyPageServiceImpl implements FarmerMyPageService {
    private final BuyMyPageRepository buyMyPageRepository;
    private final ReviewRepository reviewRepository;
    private final BidRepository bidRepository;
    private final WalletRepository walletRepository;
    private final FarmerUserRepository farmerUserRepository;
    private final FarmerRepository farmerRepository;
    private final FarmerMyPageRepository farmerMyPageRepository;
    private final PasswordEncoder passwordEncoder;

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
    public void update(FarmerUser farmerUser, Long seq) {
        log.info(farmerUser.toString());
        String pw = passwordEncoder.encode(farmerUser.getPw());
        int no = farmerUserRepository.updateUser(pw, farmerUser.getAddress(), farmerUser.getPhone(), farmerUser.getEmail(), seq);
        log.info("no={}", no);

        log.info("회원 수정 성공~");

    }

    /**
     * 회원정보 탈퇴..? 정지라고 하자
     */
    @Override
    public void delete(Long seq) {
        int i = farmerMyPageRepository.delete(seq);
        log.info("i = {}", i);
    }
}