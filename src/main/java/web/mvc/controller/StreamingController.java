package web.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.Stock;
import web.mvc.domain.Streaming;
import web.mvc.dto.StreamingDTO;
import web.mvc.service.NotificationService;
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

    @Autowired
    NotificationService notificationService;

    // 상태값이 0인 스트리밍 조회 (StreamingDTO로 반환)
    @GetMapping("/available")
    public ResponseEntity<StreamingDTO> getAvailableStreaming() {
        System.out.println("Request received: /available");
        Streaming streaming = streamingService.findAvailableStreaming();
        if (streaming != null) {
            StreamingDTO dto = convertToDTO(streaming);
            System.out.println("StreamingDTO: " + dto);
            return ResponseEntity.ok(dto);
        } else {
            System.out.println("No available streaming found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 방송 신청 엔드포인트
    @PostMapping("/request/{id}")
    public ResponseEntity<String> requestStreaming(@PathVariable Long id, @RequestParam Long farmerSeq) {
        Streaming streaming = streamingService.findById(id);
        if (streaming != null) {
            // FarmerUser가 null일 경우 초기화
            if (streaming.getFarmerUser() == null) {
                streaming.setFarmerUser(new FarmerUser());
            }

            streaming.getFarmerUser().setUserSeq(farmerSeq);
            streaming.setState(2);
            Streaming returnStreaming= streamingService.save(streaming);
            if(returnStreaming!=null){
                notificationService.sendMessageToUser(farmerSeq.toString(),"방송 신청이 완료되었습니다.");
            }
            return ResponseEntity.ok("방송 신청이 완료되었습니다. 승인 대기 중입니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("스트리밍을 찾을 수 없습니다.");
        }
    }


    // 승인 대기 중인 스트리밍 조회 (StreamingDTO로 반환)
    @GetMapping("/pending")
    public ResponseEntity<List<StreamingDTO>> getPendingStreamings() {
        List<Streaming> pendingStreamings = streamingService.findByState(2);
        if (pendingStreamings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<StreamingDTO> dtos = pendingStreamings.stream()
                .map(this::convertToDTO)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    // 방송 승인 엔드포인트
    @PostMapping("/approve/{id}")
    public ResponseEntity<String> approveStreaming(@PathVariable Long id) {
        Streaming streaming = streamingService.findById(id);
        if (streaming != null) {
            streaming.setState(1); // 상태를 사용 중으로 변경
            Streaming returnStream= streamingService.save(streaming);
            stockService.approveAllPendingStocks(); // 상품 상태도 승인 처리
            if(returnStream!=null){
                notificationService.sendMessageToUser(returnStream.getFarmerUser().getUserSeq().toString(),
                        "신청하신 방송 1건이 승인완료되었습니다.");
            }
            return ResponseEntity.ok("방송이 승인되었으며, 관련 상품도 승인되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("스트리밍을 찾을 수 없습니다.");
        }
    }

    // 활성 스트리밍 조회 (StreamingDTO로 반환)
    @GetMapping("/active-rooms")
    public ResponseEntity<List<StreamingDTO>> getActiveStreamingRooms() {
        List<StreamingDTO> activeStreamings = streamingService.streaming();
        if (activeStreamings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        System.out.println("반환되기 직전");
        return ResponseEntity.ok(activeStreamings);
    }

    // 엔티티를 DTO로 변환하는 유틸리티 메서드
    private StreamingDTO convertToDTO(Streaming streaming) {
        Long farmerSeq = (streaming.getFarmerUser() != null) ? streaming.getFarmerUser().getUserSeq() : null;

        // FarmerUser가 없을 때는 Stock 관련 정보를 빈 값으로 설정
        List<Stock> stocks = (farmerSeq != null) ? stockService.findStocksByFarmerSeq(farmerSeq) : List.of();
        Long stockSeq = stocks.isEmpty() ? null : stocks.get(0).getStockSeq();
        String productName = stocks.isEmpty() ? null : stocks.get(0).getProduct().getProductName();

        return new StreamingDTO(
                streaming.getStreamingSeq(),
                streaming.getToken(),
                streaming.getServerAddress(),
                streaming.getChatUrl(),
                streaming.getChatRoomId(),
                streaming.getPlaybackUrl(),
                streaming.getState(),
                farmerSeq,
                stockSeq,
                productName
        );
    }
    @PostMapping("/streamingData/{seq}")
    private StreamingDTO getStreamingData(@PathVariable long seq) {
        System.out.println("Request received: /streamingData/" + seq);
        StreamingDTO activeStreamings = streamingService.findByFarmerSeq(seq);
        return activeStreamings;
    }

    // 채팅방 나가기 엔드포인트
    @PostMapping("/exit/{id}")
    public ResponseEntity<String> exitStreamingRoom(@PathVariable Long id) {
        // 스트리밍 엔티티를 가져옴
        Streaming streaming = streamingService.findById(id);

        // 스트리밍 존재 여부 확인
        if (streaming != null) {
            // FarmerUser 및 상태값 초기화
            streaming.setFarmerUser(null);
            streaming.setState(0);

            // 스트리밍 엔티티 저장
            streamingService.save(streaming);

            return ResponseEntity.ok("채팅방에서 성공적으로 나왔습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("스트리밍을 찾을 수 없습니다.");
        }
    }


}
