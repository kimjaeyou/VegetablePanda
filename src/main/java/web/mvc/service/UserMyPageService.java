package web.mvc.service;

import web.mvc.domain.Bid;
import web.mvc.domain.ReviewComment;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.dto.UserBuyDTO;
import web.mvc.dto.UserDTO;
import web.mvc.dto.UserUpdateDTO;

import java.util.List;

public interface UserMyPageService {

    // 주문내역 조회
    List<UserBuyDTO> buyList(Long seq);

    // 회원정보 출력
    UserDTO selectUser(Long seq);

    // 회원정보 수정
    void update(UserUpdateDTO userUpdateDTO, Long seq);

    // 회원정보 탈퇴
    void delete (Long seq);

    // 포인트 조회
    int point(Long seq);

    // 리뷰 조회
    List<ReviewCommentDTO> review(Long seq);

    // 리뷰 삭제
    void deleteReview(Long ReviewSeq,Long userSeq);

    // 경매 내역 조회
    List<Bid> auctionList(Long seq);

}