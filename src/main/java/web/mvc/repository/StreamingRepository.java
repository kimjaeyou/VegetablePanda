package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.mvc.domain.Streaming;

import java.util.List;

@Repository
public interface StreamingRepository extends JpaRepository<Streaming, Integer> {

    // 상태값이 0인 첫 번째 스트리밍을 찾는 쿼리 메서드
    Streaming findFirstByState(Integer state);

    List<Streaming> findByState(Integer state);

    Streaming findByChatRoomId(String chatRoomId);
}
