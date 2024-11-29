package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.mvc.domain.Streaming;
import web.mvc.dto.StreamingDTO;

import java.util.List;

@Repository
public interface StreamingRepository extends JpaRepository<Streaming, Long> {

    // 상태값이 0인 첫 번째 스트리밍을 찾는 쿼리 메서드
    Streaming findFirstByState(Integer state);

    @Query("select s from Streaming s where s.state=?1")
    List<Streaming> findByState(Integer state);

    Streaming findByChatRoomId(String chatRoomId);


    @Query("select new web.mvc.dto.StreamingDTO(s.streamingSeq,s.token, s.serverAddress,s.chatUrl,s.chatRoomId,s.playbackUrl,s.state,s.farmerUser.userSeq, st.stockSeq, st.product.productName)from Streaming s join Stock st on s.farmerUser.userSeq = st.farmerUser.userSeq where s.state=1 and s.farmerUser.userSeq is not null and st.status=1")
    List<StreamingDTO> streaming();
}
