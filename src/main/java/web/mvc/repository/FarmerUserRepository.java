package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.FarmerUser;

public interface FarmerUserRepository extends JpaRepository<FarmerUser, Long> {
   FarmerUser findByUserSeq(Long userSeq);

    @Modifying
    @Query("update FarmerUser u set u.pw = :pw, u.name = :name, u.email = :email, u.phone = :phone, u.code = :code, u.address = :address where u.userSeq = :seq")
    int updateUser(
            @Param("pw") String pw,
            @Param("name") String name,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("code") String code,
            @Param("address") String address,
            @Param("seq") Long seq
    );


    //회원 탈퇴(그냥 상태값 바꾸는거임.)
    @Modifying
    @Query("update FarmerUser u set u.state = 0 where u.userSeq = ?1")
    int delete(Long seq);
}
