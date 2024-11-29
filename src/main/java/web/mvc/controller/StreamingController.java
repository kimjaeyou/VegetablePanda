    package web.mvc.controller;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import web.mvc.domain.Streaming;
    import web.mvc.dto.StreamingDTO;
    import web.mvc.service.StockServiceImpl;
    import web.mvc.service.StreamingServiceImpl;

    import java.util.List;

    @RestController
    @RequestMapping("/api/streaming")
    public class StreamingController {

        @Autowired
        private StreamingServiceImpl streamingService;
        @Autowired
        private StockServiceImpl stockService;

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
            try {
                Streaming streaming = streamingService.findById(id);
                if (streaming != null) {
                    // 스트리밍 승인
                    streaming.setState(1);
                    streamingService.save(streaming);

                    // 승인 대기중인 모든 상품들 승인
                    stockService.approveAllPendingStocks();

                    return ResponseEntity.ok("스트리밍과 대기중인 상품들이 승인되었습니다.");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("스트리밍을 찾을 수 없습니다.");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("승인 처리 중 오류가 발생했습니다: " + e.getMessage());
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


        /**
         * 상태가 1(사용 중)인 스트리밍 방 목록을 가져오는 엔드포인트
         */
        @GetMapping("/active-rooms")
        public ResponseEntity<List<StreamingDTO>> getActiveStreamingRooms() {
            List<StreamingDTO> activeStreamings = streamingService.streaming();
            for (StreamingDTO streaming : activeStreamings) {
                System.out.println(streaming.getFarmerSeq());
            }
            if (activeStreamings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(activeStreamings);
        }
    }
