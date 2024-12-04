package web.mvc.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> qaSave(
            @RequestPart("qaBoard") QaBoard qaBoard,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        log.info("QA 등록 요청: {}", qaBoard);

        // 파일 업로드 및 저장 처리
        QaDTO savedQaBoard = qaBoardService.saveQaBoard(qaBoard, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQaBoard);
    }

    /**
     * QA 수정
     */
    @PutMapping(value = "/{boardNoSeq}", consumes = "multipart/form-data")
    public ResponseEntity<?> qaUpdate(
            @PathVariable Long boardNoSeq,
            @RequestPart("qaBoard") QaDTO qaDTO,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "deleteFile", required = false, defaultValue = "false") boolean deleteFile) {
        log.info("QA 수정 요청: ID={}, 데이터={}", boardNoSeq, qaDTO);

        // 파일 업로드 및 수정 처리
        QaDTO updatedQaBoard = qaBoardService.qaUpdate(boardNoSeq, qaDTO, file, deleteFile);
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
