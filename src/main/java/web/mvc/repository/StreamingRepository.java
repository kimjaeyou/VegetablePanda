package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.mvc.domain.Streaming;

import java.util.List;

@Repository
public interface StreamingRepository extends JpaRepository<Streaming, Long> {

    // 상태값이 0인 첫 번째 스트리밍을 찾는 쿼리 메서드
    Streaming findFirstByState(Integer state);

    @Query("select s from Streaming s where s.state=?1")
    List<Streaming> findByState(Integer state);

    Streaming findByChatRoomId(String chatRoomId);


    @Query("select s from Streaming s where s.state=1 and s.farmerUser.userSeq!=0")
    List<Streaming> streaming();
}
