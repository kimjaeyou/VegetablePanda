package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.FarmerUser;

public interface FarmerUserRepository extends JpaRepository<FarmerUser, Long> {
}
