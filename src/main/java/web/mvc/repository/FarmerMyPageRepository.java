package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.User;
import web.mvc.dto.FarmerUserDTO;
import web.mvc.dto.FarmerUserDTO2;
import web.mvc.dto.OrderByBuyCountDTO;

import java.util.List;

public interface FarmerMyPageRepository extends JpaRepository<FarmerUser, Long> {

    // 수정할 회원정보 조회 값 출력
    @Query("select new web.mvc.dto.FarmerUserDTO2(" +
            "u.userSeq, u.farmerId, u.name, u.email,u.code, u.address, u.phone, u.farmerGrade.gradeContent, u.regDate, u.account, r.intro, f.path) " +
            "from FarmerUser u JOIN File f ON u.farmerId = f.name " +
            "JOIN Review r on r.managementUser.userSeq = u.userSeq " +
            "where u.userSeq = ?1")
    FarmerUserDTO2 selectUser(Long seq);

    //회원 탈퇴
    @Modifying
    @Query("update FarmerUser u set u.state = 0 where u.userSeq = ?1")
    int delete(Long seq);

    @Query("select new web.mvc.dto.FarmerUserDTO2(u.userSeq, u.name, f.path, r.intro) " +
            "from FarmerUser u " +
            "join File f on f.managementUser.userSeq = u.userSeq " +
            "join Review r on r.managementUser.userSeq = u.userSeq ")
    List<FarmerUserDTO2> farmer();


    @Query("SELECT new web.mvc.dto.FarmerUserDTO2(u.userSeq, u.name, f.path, r.intro) " +
            "FROM FarmerUser u " +
            "JOIN File f ON f.managementUser.userSeq = u.userSeq " +
            "JOIN Review r ON r.managementUser.userSeq = u.userSeq " +
            "WHERE f.managementUser.userSeq = u.userSeq")
    List<FarmerUserDTO2> fetchBasicFarmerData();






    @Query(
            value= "SELECT + s.user_seq AS userSeq, COUNT(ubd.user_buy_detail_seq) AS buyCount "+
                    "FROM user_buy_detail ubd" +
                    "JOIN stock s ON ubd.stock_seq = s.stock_seq" +
                    "GROUP BY s.user_seq" +
                    "ORDER BY buyCount DESC",nativeQuery = true)
    List<OrderByBuyCountDTO> OrderbyBuyCount();

}
