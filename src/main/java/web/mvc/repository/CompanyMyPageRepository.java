package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.User;
import web.mvc.dto.CompanyDTO;

public interface CompanyMyPageRepository extends JpaRepository<CompanyUser, Long> {

    // 수정할 회원정보 조회 값 출력
    @Query("select new web.mvc.dto.CompanyDTO(" +
            "u.userSeq, u.companyId, u.comName, u.ownerName, u.phone, u.address, u.code, u.email, u.regName, u.regDate, f.path) " +
            "from CompanyUser u LEFT JOIN File f ON u.companyId = f.name where u.userSeq = ?1")
    CompanyDTO selectUser(Long seq);

    //회원 탈퇴(그냥 상태값 바꾸는거임.)
    @Modifying
    @Query("update CompanyUser u set u.state = 0 where u.userSeq = ?1")
    int delete(Long seq);

}
