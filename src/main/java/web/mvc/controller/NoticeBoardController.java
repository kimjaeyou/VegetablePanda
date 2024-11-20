package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.NoticeBoard;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.NoticeBoardRepository;
import web.mvc.service.NoticeBoardService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/notifyBoard")
public class NoticeBoardController {
    private final NoticeBoardService noticeBoardService;
    private final NoticeBoardRepository noticeBoardRepository;

    /**
     * 공지사항 등록
     * */
    @PostMapping("")
    public ResponseEntity<?> noticeSave(@RequestBody NoticeBoard noticeBoard) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();

        if (!"Manager01".equals(currentUserId)) {
            throw new DMLException(ErrorCode.NOTFOUND_USER);
        }




        return ResponseEntity.ok(noticeBoardRepository.save(noticeBoard));
    }

    /**
     *공지사항 수정
     * */
    @PutMapping("/{notifyBoard_seq}")
    public ResponseEntity<?> noticeUpdate(@PathVariable Integer boardNoSeq, @RequestBody NoticeBoard noticeBoard) {
        return ResponseEntity.ok(noticeBoardService.noticeUpdate(boardNoSeq, noticeBoard));
    }

    /**
     *공지사항 조회
     * */
    @GetMapping("/{notifyBoard_seq}")
    public ResponseEntity<?> noticeFindBySeq(@PathVariable Integer boardNoSeq, NoticeBoard noticeBoard) {



        return ResponseEntity.ok(noticeBoardRepository.findById(boardNoSeq));
    }

    /**
     * 전체 조회
     * */
    @GetMapping("")
    public List<NoticeBoard> noticeFindAll(){


        return (List<NoticeBoard>) ResponseEntity.ok();
    }

    /**
     *공지사항 삭제
     * */
    @DeleteMapping("/{notifyBoard_seq}")
    public ResponseEntity<?> noticeDelete(@PathVariable Integer boardNoSeq) {

        return ResponseEntity.ok(noticeBoardService.noticeDelete(boardNoSeq));
    }
}
