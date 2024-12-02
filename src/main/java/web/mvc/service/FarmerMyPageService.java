package web.mvc.service;

import web.mvc.domain.FarmerUser;
import web.mvc.domain.Stock;
import web.mvc.dto.*;

import java.util.List;

public interface FarmerMyPageService {

    // 판매 내역
    List<UserBuyDTO> saleList(Long seq);

    // 회원정보 출력
    FarmerUserDTO2 selectUser(Long seq);

    // 회원정보 수정
    FarmerUser update (GetAllUserDTO getAllUserDTO, Long seq);

    // 회원정보 탈퇴
    int delete (Long reviewSeq);

    // 리뷰조회
    List<ReviewCommentDTO2> reviewList(Long seq);

    // 정산 내역
    List<CalcPoint> calcPoint(Long seq);

    // 정산 신청
    void settle(Long seq, List<CalcPoint2> list);

}
