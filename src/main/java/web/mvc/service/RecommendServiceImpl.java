package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import web.mvc.repository.ReviewCommentRepository;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {
    private final ReviewCommentRepository recRepository;
}
