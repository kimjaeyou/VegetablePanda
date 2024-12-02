package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.controller.NotificationController;
import web.mvc.domain.FarmerUser;
import web.mvc.domain.Likes;
import web.mvc.domain.Stock;
import web.mvc.dto.LikeDTO;
import web.mvc.dto.StreamingDTO;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.FarmerUserRepository;
import web.mvc.repository.LikeRepository;
import web.mvc.repository.StockRepository;
import web.mvc.repository.StreamingRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final NotificationService notificationService;
    private final StockRepository stockRepository;
    private final FarmerUserRepository farmerUserRepository;
    private final StreamingRepository streamingRepository;

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
    public List<Long> getLikeUserSeq(Long stockSeq) {
        Optional<Stock> stockOpt = stockRepository.findById(stockSeq);
        Stock stock=stockOpt.get();
        Long userSeq=stock.getFarmerUser().getUserSeq();

        List<Long> userList=likeRepository.findUserSeq(userSeq);
        Optional<FarmerUser> farmerUserOpt= farmerUserRepository.findById(userSeq);
        FarmerUser farmerUser=farmerUserOpt.get();

        for(Long user:userList){
            notificationService.sendMessageToUser(user.toString(),
                    "관심 설정한"+farmerUser.getName()+"님의 방송이 시작합니다."+
                            "///"+farmerUser.getUserSeq());
        }
        return userList;

    }


}
