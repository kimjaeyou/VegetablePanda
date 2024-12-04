package web.mvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.security.CustomMemberDetails;
import web.mvc.service.ReviewCommentService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reviewComment")
public class ReviewCommentController {

    private final ReviewCommentService reviewCommentService;
    private final ObjectMapper objectMapper;

    /**
     * 리뷰 댓글 저장
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> reviewCommentSave(
            @RequestParam Long reviewSeq,
            @RequestPart(value = "reviewCommentDTO", required = true) String reviewCommentDTOJson,
            @RequestPart(value = "image", required = false) MultipartFile file) throws JsonProcessingException {

        ReviewCommentDTO reviewCommentDTO = objectMapper.readValue(reviewCommentDTOJson, ReviewCommentDTO.class);

        ReviewCommentDTO savedComment = reviewCommentService.reviewCommentSave(reviewSeq, reviewCommentDTO, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    /**
     * 리뷰 댓글 수정
     */
    @PutMapping(value = "/{reviewCommentSeq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> reviewCommentUpdate(
            @RequestParam Long reviewSeq,
            @PathVariable Long reviewCommentSeq,
            @RequestPart(value = "reviewCommentDTO") String reviewCommentDTOJson,
            @RequestPart(value = "image", required = false) MultipartFile file,
            @RequestParam(value = "deleteFile", required = false, defaultValue = "false") boolean deleteFile) throws JsonProcessingException {

        // JSON 문자열 -> DTO 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ReviewCommentDTO reviewCommentDTO = objectMapper.readValue(reviewCommentDTOJson, ReviewCommentDTO.class);

        ReviewCommentDTO updatedComment = reviewCommentService.reviewCommentUpdate(reviewSeq, reviewCommentSeq, reviewCommentDTO, file, deleteFile);
        return ResponseEntity.ok(updatedComment);
    }

    /**
     * 리뷰 댓글 조회 - 리뷰 ID로 조회
     */
    @GetMapping("/{reviewSeq}")
    public ResponseEntity<?> reviewCommentFindByReviewSeq(@PathVariable Long reviewSeq) {

        List<ReviewCommentDTO> comments = reviewCommentService.reviewCommentFindAllByReviewId(reviewSeq);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/myComments")
    public ResponseEntity<?> reviewCommentFindByUser() {
        log.info("내가 작성한 리뷰 댓글 조회 요청");

        // 현재 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomMemberDetails userDetails = (CustomMemberDetails) authentication.getPrincipal();
        Long userSeq = userDetails.getUser().getUserSeq();

        log.info("현재 사용자 ID: {}", userSeq);

        // 서비스 계층 호출
        List<ReviewCommentDTO> myComments = reviewCommentService.reviewCommentFindAllByUserId(userSeq);

        // 결과 반환
        return ResponseEntity.ok(myComments);
    }

    /**
     * 리뷰 댓글 삭제
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{reviewCommentSeq}")
    public ResponseEntity<?> reviewCommentDelete(@PathVariable Long reviewCommentSeq) {

        reviewCommentService.reviewCommentDelete(reviewCommentSeq);
        return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
    }

    /**
     * 현재 인증된 사용자의 userSeq를 반환
     */
    private Long getAuthenticatedUserSeq() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(authentication.getName());
    }
}
