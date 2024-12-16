
package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.Review;
import web.mvc.service.ReviewService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 페이지 조회
     * 판매자 Seq를 조회해서 판매자 Seq에 맞는 리뷰 페이지를 조회
     * 리뷰 페이지는 판매자 회원가입시에 intro와 함께 생성.
     * 판매자의 리뷰 페이지를 구매자들에게 출력해주는 용도의 프론트를 위한 코드.
     * reivewComment들이 이 페이지가 조회 될 때 같이 조회되어야 함.
     * 생성은 회원가입시, 수정은 intro 수정만 하면되는데 그건 마이페이지에서 해야하고, 삭제는 회원탈퇴? 가 되면 자동으로 리뷰 페이지를 사용 하지 않을테니 삭제도 필요가없을듯 하다.
     * 그래서 조회만 남았다.
     * 조회는 어차피 판매자 1명당 1개의 리뷰페이지만 생성된다.
     * */
//    public ResponseEntity<?> findReview(@RequestParam ManagementUser managementUser, @RequestBody Review review) {
//
//        return new ResponseEntity<>(reviewService.getReviews(managementUser, review), HttpStatus.OK);
//    }


    /**
     * 리뷰 페이지 수정.. 할게 .. 있나?
     * */

//    public ResponseEntity<?> updateReview(@RequestBody Review review){
//
//        return null;
//    }


}

