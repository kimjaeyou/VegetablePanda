package web.mvc.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.NoticeBoard;

import java.util.List;

public interface NoticeBoardService {
    
    /**
     * 공지사항 등록
     * */
    public NoticeBoard noticeSave(NoticeBoard noticeBoard, MultipartFile image);

    /**
     * 공지사항 수정
     * */
    public NoticeBoard noticeUpdate(Long boardNoSeq, NoticeBoard noticeBoard, MultipartFile image, boolean deleteFile);

    /**
     * 공지사항 조회
     * */
    public NoticeBoard noticeFindBySeq(Long boardNoSeq);

    /**
     * 전체 조회
     */
    public List<NoticeBoard> noticeFindAll();

    /**
     * 공지사항 삭제
     * */
    public String noticeDelete(Long boardNoSeq);

    /**조회수*/
    NoticeBoard increaseReadnum(Long boardNoSeq);
}
