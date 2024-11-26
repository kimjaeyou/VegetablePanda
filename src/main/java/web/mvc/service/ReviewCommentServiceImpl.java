package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.ReviewComment;
import web.mvc.repository.ReviewCommentRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewCommentServiceImpl implements ReviewCommentService {

    private final ReviewCommentRepository reviewCommentRepository;

    @Override
    public ReviewComment reviewCommentSave(ReviewComment reviewComment) {


        return reviewCommentRepository.save(reviewComment);
    }

    @Override
    public ReviewComment reviewCommentUpdate(Long reviewCommentSeq, ReviewComment reviewComment) {


        return reviewCommentRepository.save(reviewComment) ;
    }

    @Override
    public ReviewComment reviewCommentFindAllById(Long reviewCommentSeq) {


        return null;
    }

    @Override
    public String reviewCommentDelete(Long reviewCommentSeq) {

            reviewCommentRepository.deleteById(reviewCommentSeq);
        return "ok";
    }
}
