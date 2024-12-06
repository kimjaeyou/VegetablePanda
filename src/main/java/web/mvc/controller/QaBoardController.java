package web.mvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import web.mvc.dto.FileDTO;
import web.mvc.dto.QaDTO;
import web.mvc.service.FileService;
import web.mvc.service.QaBoardService;
import web.mvc.service.S3ImageService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/QABoard")
public class QaBoardController {
    private final QaBoardService qaBoardService;
    private final S3ImageService s3ImageService;
    private final ObjectMapper objectMapper;

    /**
     * QA 등록
     */
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> qaSave(
            @RequestPart("qaBoard") String qaBoardJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            QaDTO qaDTO = objectMapper.readValue(qaBoardJson, QaDTO.class);
            log.info("QA 등록 요청: {}", qaDTO);

            QaDTO savedQaBoard = qaBoardService.saveQaBoard(qaDTO, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedQaBoard);

        } catch (JsonProcessingException e) {
            log.error("JSON 변환 오류: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid JSON format.");
        }
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

    /**
     * 파일 다운로드
     */
    @GetMapping("/downloadFile/{boardNoSeq}")
    public ResponseEntity<?> downloadFile(@PathVariable Long boardNoSeq) {
        log.info("파일 다운로드 요청: ID={}", boardNoSeq);

        try {
            // 서비스 계층에서 파일 정보 가져오기
            FileDTO fileDTO = qaBoardService.downloadFile(boardNoSeq);
            if (fileDTO == null || fileDTO.getPath() == null) {
                log.warn("파일이 존재하지 않거나 경로가 없습니다: ID={}", boardNoSeq);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("파일이 존재하지 않습니다.");
            }

            // S3 서비스에서 파일 다운로드
            byte[] fileData = s3ImageService.downloadFile(fileDTO.getPath());
            if (fileData == null || fileData.length == 0) {
                log.warn("파일 데이터를 다운로드할 수 없습니다: 경로={}", fileDTO.getPath());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("파일 데이터를 다운로드할 수 없습니다.");
            }

            // 헤더 설정 및 파일 반환
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileDTO.getName());

            log.info("파일 다운로드 성공: ID={}, 이름={}", boardNoSeq, fileDTO.getName());
            return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("파일 다운로드 중 오류 발생: ID={}, 오류={}", boardNoSeq, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 다운로드 중 오류가 발생했습니다.");
        }
    }

}
