package web.mvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        } catch (Exception e) {
            log.error("QA 등록 중 예기치 않은 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("QA 등록 실패.");
        }
    }

    /**
     * QA 수정
     */
    @PutMapping(value = "/{boardNoSeq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> qaUpdate(
            @PathVariable Long boardNoSeq,
            @RequestPart("qaBoard") String qaBoardJson,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "deleteFile", required = false, defaultValue = "false") boolean deleteFile) throws JsonProcessingException {

        QaDTO qaDTO = objectMapper.readValue(qaBoardJson, QaDTO.class);
        log.info("QA 수정 요청: ID={}, 데이터={}", boardNoSeq, qaDTO);

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
        try {
            FileDTO fileDTO = qaBoardService.downloadFile(boardNoSeq);
            byte[] fileData = s3ImageService.downloadFile(fileDTO.getPath());

            // 파일 확장자에 따른 MediaType 설정
            String extension = fileDTO.getName().substring(fileDTO.getName().lastIndexOf(".") + 1);
            MediaType mediaType = MediaTypeFactory
                    .getMediaType(fileDTO.getName())
                    .orElse(MediaType.APPLICATION_OCTET_STREAM);

            // 파일명 인코딩 처리
            String encodedFileName = URLEncoder.encode(fileDTO.getName(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename(encodedFileName)
                    .build());

            return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("파일 다운로드 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 다운로드에 실패했습니다.");
        }
    }

}
