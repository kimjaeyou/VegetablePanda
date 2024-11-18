package web.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.mvc.domain.Streaming;
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
    public Streaming findById(Integer id) {
        Optional<Streaming> streaming = streamingRepository.findById(id);
        return streaming.orElse(null);
    }

    @Override
    public void save(Streaming streaming) {
        streamingRepository.save(streaming);
    }
}
