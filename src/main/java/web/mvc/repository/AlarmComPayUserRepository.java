package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.CompanyUser;

public interface AlarmComPayUserRepository extends JpaRepository<CompanyUser,Long> {
}
