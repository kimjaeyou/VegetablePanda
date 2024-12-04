package web.mvc.service;

import web.mvc.domain.Streaming;
import web.mvc.dto.StreamingDTO;

import java.util.List;

public interface     StreamingService {

    // 상태값이 0인 사용 가능한 스트리밍을 찾는 메서드
    Streaming findAvailableStreaming();

    // ID로 스트리밍을 찾는 메서드
    Streaming findById(Long id);

    // 상태값을 변경하여 스트리밍을 저장하는 메서드
    Streaming save(Streaming streaming);

    List<Streaming> findByState(Integer state);

    Streaming findByChatRoomId(String chatRoomId);

    List<StreamingDTO> streaming();

    // 윤성
    List<StreamingDTO> streamingRooms(Long seq);

}
