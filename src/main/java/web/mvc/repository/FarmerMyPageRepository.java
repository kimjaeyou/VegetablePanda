package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.User;

public interface FarmerMyPageRepository extends JpaRepository<FarmerUser, Long> {

    // 수정할 회원정보 조회 값 출력
    @Query("select u from FarmerUser u where u.userSeq = ?1 ")
    FarmerUser selectUser(Long seq);

    //회원 탈퇴(그냥 상태값 바꾸는거임.)
    @Modifying
    @Query("update FarmerUser u set u.state = 0 where u.userSeq = ?1")
    int delete(Long seq);

}
