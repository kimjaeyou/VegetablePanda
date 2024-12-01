package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.NoticeBoard;
import web.mvc.service.NoticeBoardService;
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/", consumes = "multipart/form-data")
    public ResponseEntity<?> noticeSave(
            @RequestPart("noticeBoard") NoticeBoard noticeBoard,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        log.info("공지사항 등록 요청: {}", noticeBoard);

        NoticeBoard savedNotice = noticeBoardService.noticeSave(noticeBoard, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNotice);
    }

    /**
     * 공지사항 수정
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{boardNoSeq}", consumes = "multipart/form-data")
    public ResponseEntity<?> noticeUpdate(
            @PathVariable Long boardNoSeq,
            @RequestPart("noticeBoard") NoticeBoard noticeBoard,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "deleteFile", required = false, defaultValue = "false") boolean deleteFile) {
        log.info("공지사항 수정 요청: ID={}, 데이터={}", boardNoSeq, noticeBoard);

        NoticeBoard updatedNotice = noticeBoardService.noticeUpdate(boardNoSeq, noticeBoard, image, deleteFile);
        return ResponseEntity.ok(updatedNotice);
    }

    /**
     * 공지사항 조회
     */
    @GetMapping("/{boardNoSeq}")
    public ResponseEntity<?> noticeFindBySeq(@PathVariable Long boardNoSeq) {
        log.info("공지사항 단건 조회 요청: ID={}", boardNoSeq);

        return ResponseEntity.ok(noticeBoardService.noticeFindBySeq(boardNoSeq));
    }

    /**
     * 공지사항 전체 조회
     */
    @GetMapping("/")
    public ResponseEntity<List<NoticeBoard>> noticeFindAll() {
        log.info("공지사항 전체 조회 요청");

        return ResponseEntity.ok(noticeBoardService.noticeFindAll());
    }

    /**
     * 공지사항 삭제
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{boardNoSeq}")
    public ResponseEntity<?> noticeDelete(@PathVariable Long boardNoSeq) {
        log.info("공지사항 삭제 요청: ID={}", boardNoSeq);

        noticeBoardService.noticeDelete(boardNoSeq);
        return ResponseEntity.ok("공지사항이 삭제되었습니다.");
    }

    /**
     * 조회수 증가
     */
    @PutMapping("/increaseReadnum/{boardNoSeq}")
    public ResponseEntity<?> increaseReadnum(@PathVariable Long boardNoSeq) {
        log.info("조회수 증가 요청: ID={}", boardNoSeq);

        return ResponseEntity.ok(noticeBoardService.increaseReadnum(boardNoSeq));
    }
}
