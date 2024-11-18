package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.User;

public interface NormalUserRepository extends JpaRepository<User, Long> {
}
