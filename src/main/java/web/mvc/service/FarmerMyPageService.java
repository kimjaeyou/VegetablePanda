package web.mvc.service;

import web.mvc.domain.FarmerUser;

public interface FarmerMyPageService {

    // 회원정보 출력
    FarmerUser selectUser(Long seq);

    // 회원정보 수정
    void update (FarmerUser farmerUser, Long seq);

    // 회원정보 탈퇴
    void delete (Long seq);

}
