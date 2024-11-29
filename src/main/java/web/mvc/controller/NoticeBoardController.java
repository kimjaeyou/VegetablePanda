package web.mvc.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.NoticeBoard;
import web.mvc.service.NoticeBoardService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/notifyBoard")
@RequiredArgsConstructor
@Slf4j
public class NoticeBoardController {

    private final NoticeBoardService noticeBoardService;

    /**
     * 공지사항 등록
     */
    @PostMapping("/")
    public ResponseEntity<?> noticeSave(@RequestBody NoticeBoard noticeBoard) {
        validateAdminRole();
        return ResponseEntity.ok(noticeBoardService.noticeSave(noticeBoard));
    }

    /**
     * 공지사항 수정
     */
    @PutMapping("/{boardNoSeq}")
    public ResponseEntity<?> noticeUpdate(@PathVariable Long boardNoSeq, @RequestBody NoticeBoard noticeBoard) {
        validateAdminRole();
        return ResponseEntity.ok(noticeBoardService.noticeUpdate(boardNoSeq, noticeBoard));
    }

    /**
     * 공지사항 조회
     */
    @GetMapping("/{boardNoSeq}")
    public ResponseEntity<?> noticeFindBySeq(@PathVariable Long boardNoSeq) {
        return ResponseEntity.ok(noticeBoardService.noticeFindBySeq(boardNoSeq));
    }

    /**
     * 공지사항 전체 조회
     */
    @GetMapping("/")
    public ResponseEntity<List<NoticeBoard>> noticeFindAll() {
        return ResponseEntity.ok(noticeBoardService.noticeFindAll());
    }

    /**
     * 공지사항 삭제
     */
    @DeleteMapping("/{boardNoSeq}")
    public ResponseEntity<?> noticeDelete(@PathVariable Long boardNoSeq) {
        validateAdminRole();
        noticeBoardService.noticeDelete(boardNoSeq);
        return ResponseEntity.ok("공지사항이 삭제되었습니다.");
    }

    /**
     * 관리자 권한 확인
     */
    private void validateAdminRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean admin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (!admin) {
            throw new SecurityException("관리자 권한이 필요합니다.");
        }
    }

    /**
     *  조회수 증가
     * */
    @PutMapping("/increaseReadnum/{boardNoSeq}")
    public ResponseEntity<?> increaseReadnum(@PathVariable Long boardNoSeq) {
        return new ResponseEntity<>(noticeBoardService.increaseReadnum(boardNoSeq), HttpStatus.OK);
    }
}
