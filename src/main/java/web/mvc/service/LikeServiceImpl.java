package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.controller.NotificationController;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.Likes;
import web.mvc.dto.LikeDTO;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.LikeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final NotificationService notificationService;

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

    @Override
    public List<Long> getLikeUserSeq(Long aucSeq,Long userSeq) {
        List<Long> userList=likeRepository.findUserSeq(userSeq);
        FarmerUser farmerUser= likeRepository.findFarmerUserByFarmerSeq(userSeq);
        System.out.println("durl!!!!!!!!!!!!!!!!!!!!!!!!");
        for(Long user:userList){
            notificationService.sendMessageToUser(user.toString(),
                    "관심 설정한"+farmerUser.getName()+"님의 방송이 시작합니다."+
                            "///"+aucSeq);

        }
        return userList;

    }


}
