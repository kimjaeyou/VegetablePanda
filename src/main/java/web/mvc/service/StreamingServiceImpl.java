package web.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Streaming;
import web.mvc.dto.StreamingDTO;
import web.mvc.repository.StreamingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StreamingServiceImpl implements StreamingService {

    @Autowired
    private StreamingRepository streamingRepository;

    @Autowired
    NotificationService notificationService;

    @Override
    public Streaming findAvailableStreaming() {
        return streamingRepository.findFirstByState(0);
    }

    @Override
    public Streaming findById(Long id) {
        Optional<Streaming> streaming = streamingRepository.findById(id);
        return streaming.orElse(null);
    }


    @Transactional
    public Streaming exitRoomById(Long id) {
        Optional<Streaming> streamingOptional = streamingRepository.findById(id);
        Streaming streaming = streamingOptional.orElse(null);
        if(streaming != null) {
            Long farmerSeq = streaming.getFarmerUser().getUserSeq();
            streaming.setFarmerUser(null);
            streaming.setState(0);


            System.out.println("!!!!!!!!!!!: " + farmerSeq);
            notificationService.sendMessageTobidUser(farmerSeq.toString(), "방송이 종료되었습니다.");
            notificationService.sendMessageToTopic("/end/" + farmerSeq.toString() + "/notifications", "BroadCastEnd");
        }
        return streaming;
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

    // 윤성
    @Override
    public List<StreamingDTO> streamingRooms(Long seq) {
        return streamingRepository.streamingRooms(seq);
    }
}
