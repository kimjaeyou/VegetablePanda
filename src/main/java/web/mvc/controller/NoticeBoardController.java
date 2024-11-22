package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.NoticeBoard;
import web.mvc.domain.User;
import web.mvc.dto.GetAllUserDTO;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.NoticeBoardRepository;
import web.mvc.security.CustomMemberDetails;
import web.mvc.service.NoticeBoardService;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class NoticeBoardController {
    private final NoticeBoardService noticeBoardService;

    /**
     * 공지사항 등록
     * */
    @PostMapping("/notifyBoard")
    public ResponseEntity<?> noticeSave(@RequestBody NoticeBoard noticeBoard) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication = {}" , authentication);

        //시큐리티에 저장된 정보 조회
        String name = authentication.getName();//아이디
        log.info("Authentication getName =  {} " , name);
        log.info("Authentication  authentication.getPrincipal() =  {} " ,  authentication.getPrincipal());
        if(name!=null && !name.equals("anonymousUser")) {
            CustomMemberDetails customMemberDetails = (CustomMemberDetails) authentication.getPrincipal();
             GetAllUserDTO m = customMemberDetails.getUser();
            log.info("customMemberDetails =  {} ,{} ,{} ", m.getId(), m.getName(), m.getRole());


            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            Iterator<? extends GrantedAuthority> iter = authorities.iterator();
            while (iter.hasNext()) {
                GrantedAuthority auth = iter.next();
                String role = auth.getAuthority();
                log.info("Authentication role =  {} ", role);
            }

        }

        return new ResponseEntity<>(noticeBoardService.noticeSave(noticeBoard),HttpStatus.CREATED);
    }

    /**
     *공지사항 수정
     * */
    @PutMapping("/notifyBoard/{notifyBoard_seq}")
    public ResponseEntity<?> noticeUpdate(@PathVariable Long boardNoSeq, @RequestBody NoticeBoard noticeBoard) {



        return new ResponseEntity<>(noticeBoardService.noticeUpdate(boardNoSeq,noticeBoard),HttpStatus.OK);
    }

    /**
     *공지사항 조회
     * */
    @GetMapping("/notifyBoard/{notifyBoard_seq}")
    public ResponseEntity<?> noticeFindBySeq(@PathVariable Long boardNoSeq, NoticeBoard noticeBoard) {



        return new ResponseEntity<>(boardNoSeq,HttpStatus.OK);
    }

    /**
     * 전체 조회
     * */
    @GetMapping("/notifyBoard")
    public List<NoticeBoard> noticeFindAll(){


        return (List<NoticeBoard>) ResponseEntity.ok();
    }

    /**
     *공지사항 삭제
     * */
    @DeleteMapping("/notifyBoard/{notifyBoard_seq}")
    public ResponseEntity<?> noticeDelete(@PathVariable Long boardNoSeq) {

        return ResponseEntity.ok(noticeBoardService.noticeDelete(boardNoSeq));
    }
}
