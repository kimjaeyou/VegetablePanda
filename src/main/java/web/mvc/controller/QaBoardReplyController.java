package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import web.mvc.dto.QaBoardReplyDTO;
import web.mvc.service.QaBoardReplyService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/QaReplyBoard")
@RequiredArgsConstructor
@Slf4j
public class QaBoardReplyController {


    private final QaBoardReplyService qaBoardReplyService;
    
    //댓글 생성
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{boardNoSeq}")
    public ResponseEntity<QaBoardReplyDTO> saveReply(@PathVariable Long boardNoSeq, @RequestBody QaBoardReplyDTO qaBoardReplyDTO) {
        
        QaBoardReplyDTO savedReply = qaBoardReplyService.saveReply(boardNoSeq, qaBoardReplyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReply);
    }
    
    //댓글 업뎃
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{boardNoSeq}/{replySeq}")
    public ResponseEntity<QaBoardReplyDTO> updateReply(@PathVariable Long boardNoSeq, @PathVariable Long replySeq, @RequestBody QaBoardReplyDTO qaBoardReplyDTO) {
       
        QaBoardReplyDTO updatedReply = qaBoardReplyService.updateReply(boardNoSeq, replySeq, qaBoardReplyDTO);
        return ResponseEntity.ok(updatedReply);
    }
    
    
    //댓글 조회
    @GetMapping("/{boardNoSeq}")
    public ResponseEntity<List<QaBoardReplyDTO>> findRepliesByBoardId(@PathVariable Long boardNoSeq) {
      
        List<QaBoardReplyDTO> replies = qaBoardReplyService.findRepliesByBoardId(boardNoSeq);
        return ResponseEntity.ok(replies);
    }
    
    
    //댓글 삭제
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{boardNoSeq}/{replySeq}")
    public ResponseEntity<String> deleteReply(@PathVariable Long boardNoSeq, @PathVariable Long replySeq) {
       
        qaBoardReplyService.deleteReply(replySeq);
        return ResponseEntity.ok("댓글 삭제 완료");
    }
}