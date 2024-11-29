package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    /**
     * 댓글 등록
     */
    @PostMapping("/{boardNoSeq}")
    public ResponseEntity<QaBoardReplyDTO> saveReply(@PathVariable Long boardNoSeq,
                                                     @RequestBody QaBoardReplyDTO qaBoardReplyDTO) {
        log.info("댓글 등록 요청: boardNoSeq={}, comment={}", boardNoSeq, qaBoardReplyDTO.getComment());
        validateAdminRole();
        QaBoardReplyDTO savedReply = qaBoardReplyService.saveReply(boardNoSeq, qaBoardReplyDTO);
        log.info("댓글 등록 완료: replySeq={}", savedReply.getReplySeq());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReply);
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{boardNoSeq}/{replySeq}")
    public ResponseEntity<QaBoardReplyDTO> updateReply(@PathVariable Long boardNoSeq, @PathVariable Long replySeq,
                                                       @RequestBody QaBoardReplyDTO qaBoardReplyDTO) {
        log.info("댓글 수정 요청: boardNoSeq={}, replySeq={}, comment={}", boardNoSeq, replySeq, qaBoardReplyDTO.getComment());
        validateAdminRole();
        QaBoardReplyDTO updatedReply = qaBoardReplyService.updateReply(boardNoSeq, replySeq, qaBoardReplyDTO);
        log.info("댓글 수정 완료: replySeq={}", updatedReply.getReplySeq());
        return ResponseEntity.ok(updatedReply);
    }

    /**
     * 게시글별 댓글 조회
     */
    @GetMapping("/{boardNoSeq}")
    public ResponseEntity<List<QaBoardReplyDTO>> findRepliesByBoardId(@PathVariable Long boardNoSeq) {
        log.info("댓글 조회 요청: boardNoSeq={}", boardNoSeq);
        List<QaBoardReplyDTO> replies = qaBoardReplyService.findRepliesByBoardId(boardNoSeq);
        log.info("댓글 조회 완료: boardNoSeq={}, 댓글 수={}", boardNoSeq, replies.size());
        return ResponseEntity.ok(replies);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{boardNoSeq}/{replySeq}")
    public ResponseEntity<String> deleteReply(@PathVariable Long boardNoSeq, @PathVariable Long replySeq) {
        log.info("댓글 삭제 요청: boardNoSeq={}, replySeq={}", boardNoSeq, replySeq);
        validateAdminRole();
        qaBoardReplyService.deleteReply(replySeq);
        log.info("댓글 삭제 완료: replySeq={}", replySeq);
        return ResponseEntity.ok("댓글 삭제 완료");
    }

    /**
     * 관리자 권한 검증
     */
    private void validateAdminRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean admin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        }
    }
