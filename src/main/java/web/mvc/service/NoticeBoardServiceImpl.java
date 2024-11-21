package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import web.mvc.domain.NoticeBoard;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.FileRepository;
import web.mvc.repository.NoticeBoardRepository;
import web.mvc.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeBoardServiceImpl implements NoticeBoardService {
    private final NoticeBoardRepository noticeBoardRepository;
    private final FileRepository fileRepository;


    /**
     * 공지사항 등록
     * */
    @Override
    @Transactional
    public NoticeBoard noticeSave(@RequestBody NoticeBoard noticeBoard) {
        NoticeBoard savedNotice = noticeBoardRepository.save(noticeBoard);




        return noticeBoardRepository.save(savedNotice);
    }


    /**
     * 공지사항 조회
     * */
    @Override
    @Transactional
    public NoticeBoard noticeFindBySeq(@PathVariable Long boardNoSeq, @RequestBody NoticeBoard noticeBoard) {


        return noticeBoardRepository.findById(boardNoSeq).orElse(null);
    }


    /**
     * 공지사항 수정
     * */
    @Override
    @Transactional
    public NoticeBoard noticeUpdate(@PathVariable Long boardNoSeq, @RequestBody NoticeBoard noticeBoard) throws DMLException {
        NoticeBoard boardEntity = noticeBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.PRODUCT_UPDATE_FAILED));

        // 필드 업데이트
        boardEntity.setSubject(noticeBoard.getSubject());
        boardEntity.setContent(noticeBoard.getContent());
        boardEntity.setFile(noticeBoard.getFile());

        // 저장 후 반환
        return noticeBoardRepository.save(boardEntity);
    }


    /**
     * 전체 조회
     */
    @Override
    @Transactional
    public List<NoticeBoard> noticeFindAll(){


        return noticeBoardRepository.findAll();
    }


    /**
     * 공지사항 삭제
     * */
    @Override
    @Transactional
    public String noticeDelete(@PathVariable Long boardNoSeq) {

        noticeBoardRepository.deleteById(boardNoSeq);

        return "ok";
    }


}
