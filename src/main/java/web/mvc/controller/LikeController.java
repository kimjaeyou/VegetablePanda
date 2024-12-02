package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.domain.Likes;
import web.mvc.dto.BidDTO;
import web.mvc.dto.LikeDTO;
import web.mvc.exception.ErrorCode;
import web.mvc.exception.LikeException;
import web.mvc.service.LikeService;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/likeAction")
    public ResponseEntity<?> likeAction(@RequestBody LikeDTO likeDTO) {
        Likes like=likeService.like(likeDTO);
        if(like==null) {
            throw new LikeException(ErrorCode.LIKE_UPDATE_FAILED);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}