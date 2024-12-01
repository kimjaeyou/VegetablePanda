package web.mvc.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web.mvc.domain.Bid;
import web.mvc.domain.ReviewComment;
import web.mvc.domain.User;
import web.mvc.dto.*;
import web.mvc.repository.*;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class UserMyPageServiceImpl implements UserMyPageService {
    private final BuyMyPageRepository buyMyPageRepository;
    private final UserMyPageRepository userMyPageRepository;
    private final UserRepository userRepository;
    private final ManagementRepository managementRepository;
    private final ReviewRepository reviewRepository;
    private final BidRepository bidRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 주문내역
     */
    @Override
    public List<UserBuyDTO> buyList(Long seq) {
        Integer state = 1;
        return buyMyPageRepository.select(seq, state);
    }

    /**
     * 회원정보 가져오기
     */
    @Override
    public UserDTO selectUser(Long seq) {
        UserDTO user = userMyPageRepository.selectUser(seq);
        return user;
    }

    /**
     * 회원정보 수정
     */
    @Modifying
    @Override
    public User update(GetAllUserDTO getAllUserDTO, Long seq) {
        String name= getAllUserDTO.getName();
        String pw = passwordEncoder.encode(getAllUserDTO.getPw());
        String address = getAllUserDTO.getAddress();
        String phone = getAllUserDTO.getPhone();
        String email = getAllUserDTO.getEmail();
        String gender = getAllUserDTO.getGender();
        userMyPageRepository.updateUser(pw, name,email,phone, address,gender, seq);
        return userRepository.findByUserSeq(seq);
    }

    /**
     * 탈퇴
     */
    @Override
    public int delete(Long seq) {
        int i = userMyPageRepository.delete(seq);
        return i;
    }

    // 포인트 조회
    @Override
    public int point(Long seq) {
        return walletRepository.point(seq);
    }

    /**
     * 리뷰 작성 목록 조회
     */
    @Override
    public List<ReviewCommentDTO> review(Long seq) {
        Long reviewSeq = reviewRepository.selectSeq(seq);
        return reviewRepository.review(reviewSeq);
    }

    /**
     * 리뷰 삭제
     */
    @Override
    public void deleteReview(Long reviewSeq, Long userSeq) {
        int no = reviewRepository.deleteReview(reviewSeq, userSeq);
    }

    /**
     * 경매 참여한 내역을 조회
     */
    @Override
    public List<BidAuctionDTO> auctionList(Long seq) {
        List<BidAuctionDTO> list = bidRepository.auctionList(seq);
        log.info("BidAuctionDTO={}", list);
        return list;
    }
}