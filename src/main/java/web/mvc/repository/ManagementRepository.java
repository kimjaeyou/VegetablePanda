package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.User;

import java.util.List;

public interface ManagementRepository extends JpaRepository<ManagementUser, Long> {

    @Query("select count(m) from ManagementUser m where m.id=?1")
    int existsById(String id);

    ManagementUser findById(String id);

    @Query("SELECT m, t FROM ManagementUser m JOIN User t JOIN CompanyUser c Join FarmerUser f on m.content = ?1 and m.id=?2")
    List<ManagementUser> findUser(String id);

}