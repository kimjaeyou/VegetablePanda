package web.mvc.service;

import web.mvc.domain.FarmerUser;
import web.mvc.domain.ReviewComment;
import web.mvc.domain.UserBuy;
import web.mvc.dto.FarmerUserDTO;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.dto.UserBuyDTO;

import java.util.List;

public interface FarmerMyPageService {

    // 판매 내역
    List<UserBuyDTO> buyList(Long seq);

    // 회원정보 출력
    FarmerUser selectUser(Long seq);

    // 회원정보 수정
    FarmerUser update (FarmerUser farmerUser, Long seq);

    // 회원정보 탈퇴
    int delete (Long reviewSeq);

    // 리뷰조회
    List<ReviewCommentDTO> reviewList(Long seq);

    void calcPoint(Long seq , UserBuyDTO userBuyDTO);
}
