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
import web.mvc.service.QaBoardReplyServiceImpl;

import java.util.Collection;

@RestController
@RequestMapping("/notifyBoard")
@RequiredArgsConstructor
@Slf4j
public class QaBoardReplyController {

    private final QaBoardReplyService qaBoardReplyService;

    /**
     * 질문 댓글 등록
     */
    @PostMapping
    public ResponseEntity<?> qaReplySave(@RequestBody QaBoardReply qaBoardReply) {
        validateAdminRole();
        return new ResponseEntity<>(qaBoardReplyService.qaReplySave(qaBoardReply), HttpStatus.CREATED);
    }

    /**
     * 질문 댓글 수정
     */
    @PutMapping("/{boardNoSeq}")
    public ResponseEntity<?> noticeUpdate(@PathVariable Long boardNoSeq, @RequestBody QaBoardReply qaBoardReply) {
        validateAdminRole();
        return new ResponseEntity<>(qaBoardReplyService.qaReplyUpdate(boardNoSeq, qaBoardReply), HttpStatus.OK);
    }

    /**
     * 질문 댓글 조회
     */
    @GetMapping("/{boardNoSeq}")
    public ResponseEntity<?> noticeFindBySeq(@PathVariable Long boardNoSeq) {
        return new ResponseEntity<>(qaBoardReplyService.qaReplyFindBySeq(boardNoSeq), HttpStatus.OK);
    }

    /**
     * 질문 댓글 삭제
     */
    @DeleteMapping("/{boardNoSeq}")
    public ResponseEntity<?> noticeDelete(@PathVariable Long boardNoSeq) {
        validateAdminRole();
        qaBoardReplyService.qaReplyDelete(boardNoSeq);
        return ResponseEntity.ok("공지사항이 삭제되었습니다.");
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
