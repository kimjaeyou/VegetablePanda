package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.QaBoard;

public interface QaBoardRepository extends JpaRepository<QaBoard, Long> {
}
