package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import web.mvc.domain.Streaming;
import web.mvc.dto.StreamingDTO;

import java.util.List;

@Repository
public interface StreamingRepository extends JpaRepository<Streaming, Long> {

    // 상태값으로 스트리밍 검색
    List<Streaming> findByState(Integer state);

    // 상태값이 0인 첫 번째 스트리밍 찾기
    Streaming findFirstByState(Integer state);

    Streaming findByChatRoomId(String chatRoomId);


    @Query("select new web.mvc.dto.StreamingDTO(" +
            "s.streamingSeq, " +
            "s.token, " +
            "s.serverAddress, " +
            "s.chatUrl, " +
            "s.chatRoomId, " +
            "s.playbackUrl, " +
            "s.state, " +
            "s.farmerUser.userSeq, " +
            "st.stockSeq, " +
            "st.product.productName, " +
            "s.farmerUser.name, " +
            "st.file.path) " +
            "from Streaming s " +
            "join Stock st on s.farmerUser.userSeq = st.farmerUser.userSeq " +
            "where s.state=1 and s.farmerUser.userSeq is not null and st.status=1")
    List<StreamingDTO> streaming();

    // findByUserSeq와 streamingRooms도 같은 방식으로 수정
    @Query("select new web.mvc.dto.StreamingDTO(" +
            "s.streamingSeq, s.token, s.serverAddress, s.chatUrl, s.chatRoomId, " +
            "s.playbackUrl, s.state, s.farmerUser.userSeq, st.stockSeq, " +
            "st.product.productName, s.farmerUser.name, st.file.path) " +
            "from Streaming s " +
            "join Stock st on s.farmerUser.userSeq = st.farmerUser.userSeq " +
            "where s.state=1 and s.farmerUser.userSeq=:userSeq and st.status=1")
    StreamingDTO findByUserSeq(@Param("userSeq") Long userSeq);

    @Query("select new web.mvc.dto.StreamingDTO(" +
            "s.streamingSeq, s.token, s.serverAddress, s.chatUrl, s.chatRoomId, " +
            "s.playbackUrl, s.state, s.farmerUser.userSeq, st.stockSeq, " +
            "st.product.productName, s.farmerUser.name, st.file.path) " +
            "from Streaming s " +
            "join Stock st on s.farmerUser.userSeq = st.farmerUser.userSeq " +
            "where s.state=1 and s.farmerUser.userSeq is not null and st.status=1 " +
            "and s.farmerUser.userSeq = ?1")
    List<StreamingDTO> streamingRooms(Long seq);

    @Query("select s from  Streaming s where s.farmerUser.userSeq = ?1")
    Streaming findByFarmerUserSeq(Long userSeq);
}
