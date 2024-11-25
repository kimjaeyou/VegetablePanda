package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.ReviewComment;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewCommentServiceImpl implements ReviewCommentService {

    @Override
    public ReviewComment reviewCommentSave(ReviewComment reviewComment) {
        return null;
    }

    @Override
    public ReviewComment reviewCommentUpdate(Long reviewCommentSeq, ReviewComment reviewComment) {
        return null;
    }

    @Override
    public ReviewComment reviewCommentFindAllById(Long reviewCommentSeq) {
        return null;
    }

    @Override
    public ReviewComment reviewCommentDelete(Long reviewCommentSeq) {
        return null;
    }
}
