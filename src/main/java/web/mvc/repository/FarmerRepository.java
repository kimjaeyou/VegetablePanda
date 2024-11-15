package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.FarmerUser;

public interface FarmerRepository extends JpaRepository<FarmerUser, Long> {
    @Query("select u from FarmerUser u where u.farmerId = ?1")
    Boolean existsById(String id);
}