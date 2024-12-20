package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.*;
import web.mvc.dto.GetAllUserDTO;
import web.mvc.dto.UserBuyDTO;
import web.mvc.dto.UserDTO;

import java.util.List;

public interface UserMyPageRepository extends JpaRepository<User, Long> {

    // 수정할 회원정보 조회 값 출력
    @Query("SELECT new web.mvc.dto.UserDTO(u.userSeq, u.id, u.name, u.address,u.email,u.phone,u.gender,u.regDate, f.path) " +
            "FROM User u LEFT JOIN File f ON u.id = f.name " +
            "WHERE u.userSeq = ?1")
    UserDTO selectUser(Long seq);


    @Modifying
    @Query("update User u set u.state = 0 where u.userSeq = ?1")
    int delete(Long seq);

    @Modifying
    @Query("update User u set u.pw=:pw , u.name=:name , u.email=:email, u.phone=:phone , u.address=:address, u.gender=:gender where u.userSeq = :seq")
    int updateUser(@Param("pw") String pw , @Param("name") String name , @Param("email") String email , @Param("phone") String phone , @Param("address") String address , @Param("gender") String gender , Long seq);
}