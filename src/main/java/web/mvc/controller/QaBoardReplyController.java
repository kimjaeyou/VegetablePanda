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
     * 질문 댓글 등록
     */
    @PostMapping("/")
    public ResponseEntity<?> qaReplySave(@RequestBody QaBoardReply qaBoardReply) {
        validateAdminRole();
        return new ResponseEntity<>(qaBoardReplyService.qaReplySave(qaBoardReply), HttpStatus.CREATED);
    }

    /**
     * 질문 댓글 수정
     */
    @PutMapping("/{boardNoSeq}")
    public ResponseEntity<?> qaReplyUpdate(@PathVariable Long boardNoSeq, @RequestBody QaBoardReply qaBoardReply) {
        validateAdminRole();
        return new ResponseEntity<>(qaBoardReplyService.qaReplyUpdate(boardNoSeq, qaBoardReply), HttpStatus.OK);
    }


    /**
     * 질문 댓글 게시글 별 조회
     * */
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
        qaBoardReplyService.qaReplyDelete(replySeq);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
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
