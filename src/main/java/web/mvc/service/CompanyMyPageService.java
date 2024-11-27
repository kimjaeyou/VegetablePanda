package web.mvc.service;

import web.mvc.domain.Bid;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.ReviewComment;
import web.mvc.domain.User;
import web.mvc.dto.CompanyDTO;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.dto.UserBuyDTO;

import java.util.List;

public interface CompanyMyPageService {

    // 회원정보 출력
    CompanyUser selectUser(Long seq);

    // 회원정보 수정
    CompanyUser update (CompanyUser companyUser, Long seq);

    // 회원정보 탈퇴
    int delete (Long seq);

}