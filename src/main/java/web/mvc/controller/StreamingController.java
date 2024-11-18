package web.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.Streaming;
import web.mvc.service.StreamingServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/streaming")
public class StreamingController {

    @Autowired
    private StreamingServiceImpl streamingService;

    // 사용 가능한 스트리밍을 가져오는 엔드포인트
    @GetMapping("/available")
    public ResponseEntity<Streaming> getAvailableStreaming() {
        Streaming streaming = streamingService.findAvailableStreaming();
        if (streaming != null) {
            streaming.setState(1); // 상태값을 1로 설정하여 사용 중임을 표시
            streamingService.save(streaming);
            return ResponseEntity.ok(streaming);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 특정 스트리밍을 해제하는 엔드포인트
    @PostMapping("/release/{id}")
    public ResponseEntity<Void> releaseStreaming(@PathVariable Integer id) {
        Streaming streaming = streamingService.findById(id);
        if (streaming != null) {
            streaming.setState(0); // 상태값을 0으로 설정하여 사용 가능함을 표시
            streamingService.save(streaming);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
