package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Likes;
import web.mvc.dto.LikeDTO;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.LikeRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;

    @Override
    public Likes like(LikeDTO likeDTO) {
        Likes like=likeRepository.findByUserSeqAndFarmerSeq(
                likeDTO.getUserSeq(),
                likeDTO.getFarmerSeq());

        if(like!=null) {
            like.setState(!like.getState());
            like = likeRepository.save(like);
        }else{
            like = likeRepository.save(
                    new Likes(
                            likeDTO.getUserSeq(),
                            likeDTO.getFarmerSeq()));
        }


        return like;
    }

}
