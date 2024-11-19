package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.*;
import web.mvc.dto.UserBuyDTO;
import web.mvc.dto.UserDTO;

import java.util.List;

public interface UserMyPageRepository extends JpaRepository<User, Long> {

    // 수정할 회원정보 조회 값 출력
    @Query("select u from User u where u.userSeq = ?1 ")
    UserDTO selectUser(int seq);

    @Modifying
    @Query("update User u set u.state = 0")
    void delete(int state);

    @Query("select u from UserWallet u where u.managementUser.userSeq = ?1")
    int point(int seq);

    //@Query("select r from Re")
    //List<ReviewComment> review(int seq);
}
