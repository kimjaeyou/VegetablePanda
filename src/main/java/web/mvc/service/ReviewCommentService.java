package web.mvc.service;

import web.mvc.domain.ReviewComment;
import web.mvc.dto.ReviewCommentDTO;

import java.util.List;

public interface ReviewCommentService {

    //리뷰 등록
    ReviewCommentDTO reviewCommentSave (ReviewComment reviewComment);

    //리뷰 수정
    ReviewCommentDTO  reviewCommentUpdate (Long reviewCommentSeq, ReviewComment reviewComment);

    //리뷰 조회
    List<ReviewCommentDTO> reviewCommentFindAllById (Long reviewCommentSeq);

    //리뷰 삭제

    void reviewCommentDelete (Long reviewCommentSeq);




}
