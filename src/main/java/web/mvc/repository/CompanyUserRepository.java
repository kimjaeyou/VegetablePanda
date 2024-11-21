package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.User;

import java.util.List;

public interface CompanyUserRepository extends JpaRepository<CompanyUser, Long> {
    CompanyUser findByUserSeq(Long userSeq);

    @Query("select u from CompanyUser u where u.userSeq = ?1")
    CompanyUser find(Long seq);

    @Modifying
    @Query("update CompanyUser u set u.email=:email , u.pw=:pw , u.address=:addr, u.phone=:phone where u.userSeq = :seq")
    int updateUser(@Param("pw") String pw , @Param("addr")  String addr, @Param("phone") String phone, @Param("email")  String email, Long seq);

    //회원 탈퇴(그냥 상태값 바꾸는거임.)
    @Modifying
    @Query("update CompanyUser u set u.state = 0 where u.userSeq = ?1")
    int delete(Long seq);

}
