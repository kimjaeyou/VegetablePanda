package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.User;
import web.mvc.dto.GetAllUserDTO;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select count(m) from ManagementUser m where m.id=?1")
    int existsById(String id);

    @Query("select u from User u where u.id = ?1")
    GetAllUserDTO findById(String id);
}