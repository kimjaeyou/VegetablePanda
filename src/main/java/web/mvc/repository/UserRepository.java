package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import web.mvc.domain.ManagementUser;
import web.mvc.domain.User;

public interface UserRepository extends JpaRepository<ManagementUser, Long> {
    @Query("select count(m) from ManagementUser m where m.id=?1")
    int existsById(String id);

    @Query("select m from ManagementUser m where m.id=?1")
    ManagementUser findById(String id);

    User findByUserSeq(Long userSeq);

}
