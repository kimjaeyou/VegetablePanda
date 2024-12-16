package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.domain.Likes;
import web.mvc.dto.BidDTO;
import web.mvc.dto.LikeDTO;
import web.mvc.exception.ErrorCode;
import web.mvc.exception.LikeException;
import web.mvc.service.BidService;
import web.mvc.service.LikeService;
import web.mvc.service.LikeServiceImpl;
import web.mvc.service.NotificationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LikeController {
    private final LikeService likeService;
    private final LikeServiceImpl likeServiceImpl;
    private final NotificationService notificationService;

    @PostMapping("/likeAction")
    public ResponseEntity<?> likeAction(@RequestBody LikeDTO likeDTO) {
        Likes like=likeService.like(likeDTO);
        if(like==null) {
            throw new LikeException(ErrorCode.LIKE_UPDATE_FAILED);
        }else{
            if(like.getState()){
                notificationService.sendMessageToUser(
                        like.getManagementUser().getUserSeq().toString(),
                        "구독 성공 하셨습니다.");
            }else{
                notificationService.sendMessageToUser(
                        like.getManagementUser().getUserSeq().toString(),
                        "구독 취소 하셨습니다.");
            }

        }
        return new ResponseEntity<>(like,HttpStatus.CREATED);
    }

    @PostMapping("/likeState")
    public ResponseEntity<?> likeState(@RequestBody LikeDTO likeDTO) {
        System.out.println("여기 : "+likeDTO);
        return new ResponseEntity<>(likeService.likeState(likeDTO) ,HttpStatus.OK);
    }

    // 구독중인 판매자 목록 가져오기
    @GetMapping("/like/list")
    public ResponseEntity<?> likeList(Long userSeq) {
        List<LikeDTO> list = likeService.getLikeFarmer(userSeq);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}