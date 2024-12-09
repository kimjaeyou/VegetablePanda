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
            "from FarmerUser u JOIN ManagementUser m on u.userSeq = m.userSeq " +
            "JOIN File f ON m.file.fileSeq = f.fileSeq " +
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
            value = """
        SELECT new web.mvc.dto.OrderByBuyCountDTO(
            s.farmerUser.userSeq, COUNT(ubd.userBuySeq)
        )
        FROM UserBuyDetail ubd
        JOIN ubd.stock s
        GROUP BY s.farmerUser.userSeq
        ORDER BY COUNT(ubd.userBuySeq) DESC
    """
    )
    List<OrderByBuyCountDTO> OrderbyBuyCount();

    @Query("""
    SELECT new web.mvc.dto.FarmerUserDTO2(
        u.userSeq, 
        u.name, 
        f.path, 
        r.intro
    )
    FROM FarmerUser u
    JOIN File f ON f.managementUser.userSeq = u.userSeq
    JOIN Review r ON r.managementUser.userSeq = u.userSeq
    LEFT JOIN UserBuyDetail ubd ON ubd.stock.farmerUser.userSeq = u.userSeq
    GROUP BY u.userSeq
    ORDER BY COUNT(ubd.userBuySeq) DESC
""")
    List<FarmerUserDTO2> fetchSortedFarmerData();



}
