package web.mvc.service;

import web.mvc.domain.Likes;
import web.mvc.dto.LikeDTO;

public interface LikeService {
    Likes like(LikeDTO likeDTO);
}
