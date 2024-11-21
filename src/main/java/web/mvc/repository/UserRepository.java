package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.User;
import web.mvc.dto.UserDTO;

import web.mvc.domain.ManagementUser;
import web.mvc.domain.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select count(m) from ManagementUser m where m.id=?1")
    int existsById(String id);

    @Query("select m from ManagementUser m where m.id=?1")
    ManagementUser findById(String id);

    @Query("select u from User u where u.userSeq = ?1")
    User findByUserSeq(Long userSeq);

    @Query("select u from User u where u.userSeq = ?1")
    List<User> findListByUserSeq(Long userSeq);

    @Modifying
    @Query("update User u set u.email=:email , u.pw=:pw , u.address=:addr, u.phone=:phone, u.gender=:gender where u.userSeq = :seq")
    int updateUser(@Param("pw") String pw ,@Param("addr")  String addr, @Param("phone") String phone,@Param("email")  String email,@Param("gender") String gender, Long seq);

}
