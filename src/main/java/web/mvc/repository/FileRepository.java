package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.File;

public interface FileRepository extends JpaRepository<File,Integer> {
}
