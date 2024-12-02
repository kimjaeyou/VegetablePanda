package web.mvc.service;

import web.mvc.domain.Likes;
import web.mvc.dto.LikeDTO;

import java.util.List;

public interface LikeService {
    Likes like(LikeDTO likeDTO);
    List<Long> getLikeUserSeq(Long userSeq);
}
