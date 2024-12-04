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
import web.mvc.service.LikeService;
import web.mvc.service.LikeServiceImpl;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LikeController {
    private final LikeService likeService;
    private final LikeServiceImpl likeServiceImpl;

    @PostMapping("/likeAction")
    public ResponseEntity<?> likeAction(@RequestBody LikeDTO likeDTO) {
        Likes like=likeService.like(likeDTO);
        if(like==null) {
            throw new LikeException(ErrorCode.LIKE_UPDATE_FAILED);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/likeState")
    public ResponseEntity<?> likeState(@RequestBody LikeDTO likeDTO) {
        return new ResponseEntity<>(likeService.likeState(likeDTO) ,HttpStatus.OK);
    }
}