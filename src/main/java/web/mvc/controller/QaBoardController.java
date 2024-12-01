package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.QaBoard;
import web.mvc.dto.QaDTO;
import web.mvc.service.QaBoardService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/QABoard")
public class QaBoardController {
    private final QaBoardService qaBoardService;

    /**
     * QA 등록
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/", consumes = "multipart/form-data")
    public ResponseEntity<?> qaSave(
            @RequestPart("qaBoard") QaDTO qaDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        log.info("QA 등록 요청: {}", qaDTO);

        QaDTO savedQaBoard = qaBoardService.qaSave(qaDTO, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQaBoard);
    }

    /**
     * QA 수정
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/{boardNoSeq}", consumes = "multipart/form-data")
    public ResponseEntity<?> qaUpdate(
            @PathVariable Long boardNoSeq,
            @RequestPart("qaBoard") QaDTO qaDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "deleteFile", required = false, defaultValue = "false") boolean deleteFile) {
        log.info("QA 수정 요청: ID={}, 데이터={}", boardNoSeq, qaDTO);

        QaDTO updatedQaBoard = qaBoardService.qaUpdate(boardNoSeq, qaDTO, image, deleteFile);
        return ResponseEntity.ok(updatedQaBoard);
    }

    /**
     * QA 단건 조회
     */
    @GetMapping("/{boardNoSeq}")
    public ResponseEntity<?> qaFindBySeq(@PathVariable Long boardNoSeq) {
        log.info("QA 단건 조회 요청: ID={}", boardNoSeq);

        QaDTO qaDTO = qaBoardService.qaFindBySeq(boardNoSeq);
        return ResponseEntity.ok(qaDTO);
    }

    /**
     * QA 전체 조회
     */
    @GetMapping("/")
    public ResponseEntity<List<QaDTO>> qaFindAll() {
        log.info("QA 전체 조회 요청");

        List<QaDTO> qaBoardList = qaBoardService.qaFindAll();
        return ResponseEntity.ok(qaBoardList);
    }

    /**
     * QA 삭제
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{boardNoSeq}")
    public ResponseEntity<?> qaDelete(@PathVariable Long boardNoSeq) {
        log.info("QA 삭제 요청: ID={}", boardNoSeq);

        qaBoardService.qaDelete(boardNoSeq);
        return ResponseEntity.ok("QA가 삭제되었습니다.");
    }

    /**
     * 조회수 증가
     */
    @PutMapping("/increaseReadnum/{boardNoSeq}")
    public ResponseEntity<?> increaseReadnum(@PathVariable Long boardNoSeq) {
        log.info("조회수 증가 요청: ID={}", boardNoSeq);

        QaBoard updatedQaBoard = qaBoardService.increaseReadnum(boardNoSeq);
        return ResponseEntity.ok(updatedQaBoard);
    }
}
