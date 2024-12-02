package web.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.mvc.domain.Streaming;
import web.mvc.dto.StreamingDTO;
import web.mvc.repository.StreamingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StreamingServiceImpl implements StreamingService {

    @Autowired
    private StreamingRepository streamingRepository;

    @Override
    public Streaming findAvailableStreaming() {
        return streamingRepository.findFirstByState(0);
    }

    @Override
    public Streaming findById(Long id) {
        Optional<Streaming> streaming = streamingRepository.findById(id);
        return streaming.orElse(null);
    }

    @Override
    public Streaming save(Streaming streaming) {
        return streamingRepository.save(streaming);
    }

    public StreamingDTO findByFarmerSeq(long farmerSeq) {
        StreamingDTO streamingDTO=streamingRepository.findByUserSeq(farmerSeq);
        return streamingDTO;
    }


    @Override
    public List<Streaming> findByState(Integer state) {
        return streamingRepository.findByState(state);
    }

    @Override
    public Streaming findByChatRoomId(String chatRoomId) {
        return streamingRepository.findByChatRoomId(chatRoomId);
    }

    @Override
    public List<StreamingDTO> streaming() {
        return streamingRepository.streaming();
    }
}
