package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.User;

public interface ManagementRepository extends JpaRepository<ManagementUser, Long> {

    @Query("select u.id from ManagementUser u where u.id = ?1")
    Boolean existsById(String id);

    ManagementUser findById(String id);

}