package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.CompanyUser;

public interface CompanyUserRepository extends JpaRepository<CompanyUser, Long> {
    CompanyUser findByUserSeq(Long userSeq);
}
