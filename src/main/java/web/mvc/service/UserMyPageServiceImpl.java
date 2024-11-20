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
import web.mvc.dto.UserBuyDTO;
import web.mvc.dto.UserDTO;
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
    private final PasswordEncoder passwordEncoder;

    /**
     * 주문내역
     */
    @Override
    public List<UserBuyDTO> buyList(Long seq) {
        /**
         * 일단 user_buy 테이블에서 유저시퀀스가 맞는 데이터를 찾는다.
         * 근데 여기서 user_buy랑 user_buy_detail이랑 조인해서 해당되는 값들을 List에 담아서 가져가자
         * 아마 보여줄것들이
         * 시퀀스값이랑, 가격, 상품명, 구매날짜,,, 이정도,,,?
         */
        return buyMyPageRepository.selectAll(seq);
    }

    /**
     * 회원정보 가져오기
     */
    @Override
    public User selectUser(Long seq) {
        User user = userMyPageRepository.selectUser(seq);
        return user;
    }

    /**
     * 회원정보 수정
     */
    @Modifying
    @Override
    public void update(User user, Long seq) {
        log.info(user.toString());

        // 일단 시퀀스에 해당하는 값 찾아서 값만 바꿔주자
        User user1 = userRepository.find(seq);
        log.info(user.getGender()); // 여기까지 나옴

        int no = userRepository.updateUser(user1.getPw(), user1.getAddress(),user1.getGender(),user1.getPhone(), user1.getEmail(), seq);
        log.info("no={}",no);
        log.info("회원 수정 성공~");

    }

    /**
     * 회원정보 탈퇴..? 정지라고 하자
     */
    @Override
    public void delete(int state) {
        userMyPageRepository.delete(state);
    }

    // 포인트 조회
    @Override
    public int point(Long seq) {
        return userMyPageRepository.point(seq);
    }

    /**
     * 리뷰 작성 목록 조회
     */
    @Override
    public List<ReviewComment> review(Long seq) {
        // 처음에 유저 시퀀스에 해당하는 review 시퀀스를 가져오자
        Long reviewSeq = reviewRepository.selectSeq(seq);

        return reviewRepository.review(reviewSeq);
    }

    /**
     * 리뷰 삭제
     */
    @Override
    public void deleteReview(Long seq) {
        reviewRepository.deleteReview(seq);
    }

    /**
     * 경매 참여한 내역을 조회
     * 그러면 일단 먼저 시퀀스로 값을 찾아서 목록 가져오기?
     */
    @Override
    public List<Bid> auctionList(Long seq) {
        List <Bid> list = bidRepository.auctionList(seq);
        return list;
    }
}

