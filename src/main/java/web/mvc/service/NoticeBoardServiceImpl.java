package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.NoticeBoard;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.NoticeBoardRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeBoardServiceImpl implements NoticeBoardService {

    private final NoticeBoardRepository noticeBoardRepository;

    /**
     * 공지사항 등록
     */
    @Override
    @Transactional
    public NoticeBoard noticeSave(NoticeBoard noticeBoard) {
        log.info("공지사항 등록 요청: {}", noticeBoard);
        return noticeBoardRepository.save(noticeBoard);
    }

    /**
     * 공지사항 조회
     */
    @Override
    @Transactional(readOnly = true)
    public NoticeBoard noticeFindBySeq(Long boardNoSeq) {
        log.info("공지사항 단건 조회 요청: ID={}", boardNoSeq);
        return noticeBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));
    }

    /**
     * 공지사항 수정
     */
    @Override
    @Transactional
    public NoticeBoard noticeUpdate(Long boardNoSeq, NoticeBoard noticeBoard) {
        log.info("공지사항 글번호: ID={}, 내용={}", boardNoSeq, noticeBoard);

        NoticeBoard Notice = noticeBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.UPDATE_FAILED));

        Notice.setSubject(noticeBoard.getSubject());
        Notice.setContent(noticeBoard.getContent());

        return noticeBoardRepository.save(Notice);
    }

    /**
     * 공지사항 전체 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<NoticeBoard> noticeFindAll() {
        log.info("공지사항 전체 조회 요청");
        return noticeBoardRepository.findAll();
    }

    /**
     * 공지사항 삭제
     */
    @Override
    @Transactional
    public String noticeDelete(Long boardNoSeq) {
        log.info("공지사항 글번호={}", boardNoSeq);

        NoticeBoard noticeBoard = noticeBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        noticeBoardRepository.delete(noticeBoard);
        log.info("공지사항 삭제 성공: ID={}", boardNoSeq);

        return "정상적으로 삭제되었습니다.";
    }

    @Override
    public NoticeBoard increaseReadnum(Long boardNoSeq) {
        NoticeBoard ntBoard = noticeBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        ntBoard.setReadnum(ntBoard.getReadnum() + 1);
        return noticeBoardRepository.save(ntBoard);
    }
}
