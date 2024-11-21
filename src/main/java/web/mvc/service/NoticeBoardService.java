package web.mvc.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import web.mvc.domain.NoticeBoard;

import java.util.List;

public interface NoticeBoardService {
    
    /**
     * 공지사항 등록
     * */
    public NoticeBoard noticeSave(@RequestBody NoticeBoard noticeBoard);

    /**
     * 공지사항 수정
     * */
    public NoticeBoard noticeUpdate(@PathVariable Long boardNoSeq, @RequestBody NoticeBoard noticeBoard);

    /**
     * 공지사항 조회
     * */
    public NoticeBoard noticeFindBySeq(@PathVariable Long boardNoSeq,NoticeBoard noticeBoard);

    /**
     * 전체 조회
     */
    public List<NoticeBoard> noticeFindAll();

    /**
     * 공지사항 삭제
     * */
    public String noticeDelete(@PathVariable Long boardNoSeq);
    
}
