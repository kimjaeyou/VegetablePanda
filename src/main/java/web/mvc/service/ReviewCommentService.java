package web.mvc.service;

import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.ReviewComment;
import web.mvc.dto.ReviewCommentDTO;

import java.util.List;

public interface ReviewCommentService {

    // 댓글 등록
    ReviewCommentDTO reviewCommentSave(Long reviewSeq, ReviewCommentDTO reviewCommentDTO, MultipartFile file);

    // 댓글 수정
    ReviewCommentDTO reviewCommentUpdate(Long reviewSeq, Long reviewCommentSeq, ReviewCommentDTO reviewCommentDTO, MultipartFile file, boolean deleteFile);

    // 특정 사용자가 작성한 댓글 조회
    List<ReviewCommentDTO> reviewCommentFindAllByUserId(Long userId);

    // 특정 게시글에 대한 댓글 조회
    List<ReviewCommentDTO> reviewCommentFindAllByReviewId(Long reviewSeq);

    // 댓글 삭제
    void reviewCommentDelete(Long reviewCommentSeq);




}
