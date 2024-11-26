package web.mvc.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web.mvc.domain.Bid;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.ReviewComment;
import web.mvc.domain.User;
import web.mvc.dto.CompanyDTO;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.dto.UserBuyDTO;
import web.mvc.repository.*;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class CompanyMyPageServiceImpl implements CompanyMyPageService {
    private final BuyMyPageRepository buyMyPageRepository;
    private final ReviewRepository reviewRepository;
    private final BidRepository bidRepository;
    private final WalletRepository walletRepository;
    private final CompanyMyPageRepository companyMyPageRepository;
    private final CompanyUserRepository companyUserRepository;
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
    public CompanyUser selectUser(Long seq) {
        CompanyUser companyUser = companyMyPageRepository.selectUser(seq);
        return companyUser;
    }

    /**
     * 회원정보 수정
     */
    @Modifying
    @Override
    public void update(CompanyUser companyUser, Long seq) {
        log.info(companyUser.toString());
        String pw = passwordEncoder.encode(companyUser.getPw());
        int no = companyUserRepository.updateUser(pw, companyUser.getAddress(), companyUser.getPhone(), companyUser.getEmail(), seq);
        log.info("no={}", no);

        log.info("회원 수정 성공~");

    }

    /**
     * 회원정보 탈퇴..? 정지라고 하자
     */
    @Override
    public void delete(Long seq) {
        int i = companyMyPageRepository.delete(seq);
        log.info("i = {}", i);
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
        // 처음에 유저 시퀀스에 해당하는 review 시퀀스를 가져오자
        Long reviewSeq = reviewRepository.selectSeq(seq);

        // 그럼 그 리뷰 시퀀스에 해당하는 리뷰들을 가져오자
        return reviewRepository.review(reviewSeq);
    }

    /**
     * 리뷰 삭제
     */
    @Override
    public void deleteReview(Long reviewSeq, Long userSeq) {
        int no = reviewRepository.deleteReview(reviewSeq, userSeq);
        log.info("no = {}", no);
    }

    /**
     * 경매 참여한 내역을 조회
     * 그러면 일단 먼저 시퀀스로 값을 찾아서 목록 가져오기?
     */
    @Override
    public List<Bid> auctionList(Long seq) {
        List<Bid> list = bidRepository.auctionList(seq);
        return list;
    }

    public CompanyDTO selectCompany(Long seq) {
        CompanyUser company = companyUserRepository.findById(seq)
                .orElseThrow(() -> new RuntimeException("업체 회원을 찾을 수 없습니다."));

        return CompanyDTO.builder()
                .userSeq(company.getUserSeq())
                .companyId(company.getCompanyId())
                .comName(company.getComName())
                .ownerName(company.getOwnerName())
                .phone(company.getPhone())
                .address(company.getAddress())
                .code(company.getCode())
                .email(company.getEmail())
                .build();
    }
}