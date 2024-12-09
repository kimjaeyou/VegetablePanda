package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.User;
import web.mvc.dto.FarmerUserDTO;
import web.mvc.dto.FarmerUserDTO2;

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

    @Query("select new web.mvc.dto.FarmerUserDTO2(u.userSeq, u.name, f.path, r.intro) from FarmerUser u " +
            "join File f on f.managementUser.userSeq = u.userSeq " +
            "join Review r on r.managementUser.userSeq = u.userSeq ")
    List<FarmerUserDTO2> farmer();
}
