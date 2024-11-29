package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.QaBoardReply;
import web.mvc.dto.QAReplyDTO;
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
      * 댓글등록
      * */
    @PostMapping("/")
    public ResponseEntity<?> qaReplySave(@RequestBody QAReplyDTO qaReplyDTO) {
        validateAdminRole();
        QaBoardReply savedReply = qaBoardReplyService.qaReplySave(qaReplyDTO);
        return new ResponseEntity<>(savedReply, HttpStatus.CREATED);
    }

    /**
     * 질문 댓글 수정
     */
    @PutMapping("/{boardNoSeq}")
    public ResponseEntity<?> noticeUpdate(@PathVariable Long boardNoSeq, @RequestBody QAReplyDTO qaReplyDTO) {
        validateAdminRole();
        QaBoardReply updatedReply = qaBoardReplyService.qaReplyUpdate(boardNoSeq, qaReplyDTO);
        return new ResponseEntity<>(updatedReply, HttpStatus.OK);
    }

    /**
     * 질문 댓글 게시글 별 조회
     */
    @GetMapping("/{boardNoSeq}")
    public ResponseEntity<?> qaFindAllById(@PathVariable Long boardNoSeq) {
        List<QaBoardReply> replies = qaBoardReplyService.qaFindAllById(boardNoSeq);
        return new ResponseEntity<>(replies, HttpStatus.OK);
    }

    /**
     * 질문 댓글 삭제
     */
    @DeleteMapping("/{replySeq}")
    public ResponseEntity<?> noticeDelete(@PathVariable Long replySeq) {
        validateAdminRole();
        String result = qaBoardReplyService.qaReplyDelete(replySeq);
        return ResponseEntity.ok(result);
    }

    private void validateAdminRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean admin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (!admin) {
            throw new SecurityException("관리자 권한이 필요합니다.");
        }
    }
}
