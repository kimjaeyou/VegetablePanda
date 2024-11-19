package web.mvc.service;

import web.mvc.domain.ReviewComment;
import web.mvc.dto.UserBuyDTO;
import web.mvc.dto.UserDTO;

import java.util.List;

public interface UserMyPageService {

    // 주문내역 조회
    List<UserBuyDTO> buyList(int seq);

    // 회원정보 출력
    UserDTO selectUser(int seq);

    // 회원정보 수정
    void update (UserDTO userDTO);

    // 회원정보 탈퇴
    void delete (int state);

    // 포인트 조회
    int point(int seq);

    // 리뷰 조회
  //  List<ReviewComment> review(int seq);
}
