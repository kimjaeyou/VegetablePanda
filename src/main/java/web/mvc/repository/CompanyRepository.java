package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.User;

public interface CompanyRepository extends JpaRepository<CompanyUser, Long> {
    @Query("select u from CompanyUser u where u.companyId = ?1")
    Boolean existsById(String id);
}