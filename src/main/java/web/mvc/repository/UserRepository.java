package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.User;

import web.mvc.domain.ManagementUser;
import web.mvc.domain.User;
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserSeq(Long userSeq);

    @Query("select u from User u where u.id = ?1")
    User findById(String id);
}
