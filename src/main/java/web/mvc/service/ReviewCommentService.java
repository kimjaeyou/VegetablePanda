package web.mvc.service;

import web.mvc.domain.ReviewComment;

public interface ReviewCommentService {

    //리뷰 등록
    ReviewComment reviewCommentSave (ReviewComment reviewComment);

    //리뷰 수정
    ReviewComment reviewCommentUpdate (Long reviewCommentSeq, ReviewComment reviewComment);

    //리뷰 조회
    ReviewComment reviewCommentFindAllById (Long reviewCommentSeq);

    //리뷰 삭제

    String reviewCommentDelete (Long reviewCommentSeq);




}
