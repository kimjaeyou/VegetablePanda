package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.ManagementUser;

public interface FammerUserRepository extends JpaRepository<FarmerUser, Long> {
}
