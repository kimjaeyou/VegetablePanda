package web.mvc.service;

import web.mvc.domain.Member;

public interface MemberService {
    String duplicateCheck(String id);

    void signUp(Member member);
}
