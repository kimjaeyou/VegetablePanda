package web.mvc.service;

import web.mvc.domain.Bid;
import web.mvc.domain.ReviewComment;
import web.mvc.domain.User;
import web.mvc.dto.*;

import java.util.List;

public interface UserMyPageService {

    // 주문내역 조회
    List<UserBuyListForReivewDTO> buyList(Long seq);
    
    //업체유저 주문내역 조회
    List<UserBuyListForReivewDTO> companyAuctionBuyList(Long seq);

    // 회원정보 출력
    UserDTO selectUser(Long seq);

    // 회원정보 수정
    User update (GetAllUserDTO getAllUserDTO, Long seq);

    // 회원정보 탈퇴
    int delete (Long seq);

    // 포인트 조회
    int point(Long seq);

    // 리뷰 조회
    List<ReviewCommentDTO> review(Long seq);

    // 리뷰 삭제
    void deleteReview(Long ReviewSeq,Long userSeq);

    // 경매 내역 조회
    List<BidAuctionDTO> auctionList(Long seq);

    // 좋아요 목록
    List<LikeDTO> likeList(Long seq);

    // 좋아요 취소
    String likeDelete(Long seq,Long likeSeq);

    // 구독 목록
    List<UserLikeDTO> userLikeList(Long seq);

    // 구독취소
    String userLikeDelete(Long seq, Long userLikeSeq);

    // 경매 낙찰 내역 조회
    List<BidAuctionDTO> successfulBidList (Long seq);
}
