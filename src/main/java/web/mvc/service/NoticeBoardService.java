package web.mvc.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.NoticeBoard;
import web.mvc.dto.NoticeBoardDTO;

import java.util.List;

public interface NoticeBoardService {
    
    /**
     * 공지사항 등록
     * */
    public NoticeBoardDTO noticeSave(NoticeBoardDTO noticeBoardDTO, MultipartFile image);

    /**
     * 공지사항 수정
     * */
    public NoticeBoardDTO noticeUpdate(Long boardNoSeq, NoticeBoardDTO noticeBoardDTO, MultipartFile image, boolean deleteFile);

    /**
     * 공지사항 조회
     * */
    public NoticeBoardDTO noticeFindBySeq(Long boardNoSeq);

    /**
     * 전체 조회
     */
    public List<NoticeBoardDTO> noticeFindAll();

    /**
     * 공지사항 삭제
     * */
    public String noticeDelete(Long boardNoSeq);

    /**조회수*/
    NoticeBoardDTO increaseReadnum(Long boardNoSeq);
}
