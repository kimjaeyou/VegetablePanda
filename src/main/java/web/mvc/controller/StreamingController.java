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

        /**
         * 사용 가능한 스트리밍을 가져오는 엔드포인트
         */
        @GetMapping("/available")
        public ResponseEntity<Streaming> getAvailableStreaming() {
            Streaming streaming = streamingService.findAvailableStreaming();
            if (streaming != null) {
                streaming.setState(1);
                streamingService.save(streaming);
                return ResponseEntity.ok(streaming);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
        }
        /**
         * 특정 채팅방의 상태를 조회하는 엔드포인트
         */
        @GetMapping("/status")
        public ResponseEntity<Streaming> getStreamingStatus(@RequestParam String roomId) {
            Streaming streaming = streamingService.findByChatRoomId(roomId);
            if (streaming != null) {
                return ResponseEntity.ok(streaming);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }

        /**
         * 특정 스트리밍을 해제하는 엔드포인트
         * 상태를 0으로 변경
         */
        @PostMapping("/release/{id}")
        public ResponseEntity<String> releaseStreaming(@PathVariable Long id) {
            Streaming streaming = streamingService.findById(id);
            if (streaming != null) {
                streaming.setState(0);
                streamingService.save(streaming);
                return ResponseEntity.ok("Streaming room released successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Streaming room not found.");
            }
        }

        /**
         * 스트리밍 승인 대기 상태로 요청 처리
         * 상태를 2로 변경 (승인 대기)
         */
        @PostMapping("/request/{id}")
        public ResponseEntity<String> requestStreaming(@PathVariable Long id) {
            Streaming streaming = streamingService.findById(id);
            if (streaming != null) {
                streaming.setState(2);
                streamingService.save(streaming);
                return ResponseEntity.ok("Streaming room request submitted.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Streaming room not found.");
            }
        }

        /**
         * 스트리밍 승인 처리
         * 상태를 1로 변경 (사용 중)
         */
        @PostMapping("/approve/{id}")
        public ResponseEntity<String> approveStreaming(@PathVariable Long id) {
            Streaming streaming = streamingService.findById(id);
            if (streaming != null) {
                streaming.setState(1);
                streamingService.save(streaming);
                return ResponseEntity.ok("Streaming room approved successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Streaming room not found.");
            }
        }

        /**
         * 승인 대기 중인 스트리밍을 가져오는 엔드포인트
         * 상태가 2인 스트리밍만 반환
         */
        @GetMapping("/pending")
        public ResponseEntity<List<Streaming>> getPendingStreamings() {
            List<Streaming> pendingStreamings = streamingService.findByState(2);
            if (pendingStreamings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(pendingStreamings);
        }
    }
