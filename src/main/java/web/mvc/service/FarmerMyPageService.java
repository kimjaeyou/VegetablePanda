package web.mvc.service;

import web.mvc.domain.FarmerUser;
import web.mvc.dto.*;

import java.util.List;

public interface FarmerMyPageService {

    // 판매 내역
    List<UserBuyDTO> saleList(Long seq);

    // 회원정보 출력
    FarmerUser selectUser(Long seq);

    // 회원정보 수정
    FarmerUser update (FarmerUser farmerUser, Long seq);

    // 회원정보 탈퇴
    int delete (Long reviewSeq);

    // 리뷰조회
    List<ReviewCommentDTO2> reviewList(Long seq);

    List<CalcPoint> calcPoint(Long seq);

    void settle(Long seq, List<CalcPoint> list);
}
