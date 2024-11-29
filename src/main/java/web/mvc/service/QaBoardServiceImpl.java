package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.NoticeBoard;
import web.mvc.domain.QaBoard;
import web.mvc.dto.QaDTO;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.QaBoardRepository;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class QaBoardServiceImpl implements QaBoardService {

    private final QaBoardRepository qaBoardRepository;

    /**
     * 질문 등록
     * */
    @Override
    public QaDTO qaSave(QaBoard qaBoard) {
        String writerId = getCurrentUserId();

        qaBoard.setReadnum(0);
        qaBoard.setFile(null);
        qaBoard.getManagementUser().setId(writerId); // 작성자 설정

        QaBoard savedQaBoard = qaBoardRepository.save(qaBoard);
        return QaDTO.fromEntity(savedQaBoard, writerId);
    }


    /**
     * 질문 수정
     * */
    @Override
    public QaDTO qaUpdate(Long boardNoSeq, QaBoard qaBoard) {
        QaBoard qa = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        qa.setSubject(qaBoard.getSubject());
        qa.setContent(qaBoard.getContent());

        return QaDTO.fromEntity(qaBoardRepository.save(qa), qa.getManagementUser().getId()); // 작성자 ID 유지
    }


    /**
     * 질문 조회
     * */
    @Override
    @Transactional(readOnly = true)
    public QaDTO qaFindBySeq(Long boardNoSeq) {
        QaBoard qaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));
        return QaDTO.fromEntity(qaBoard, qaBoard.getManagementUser().getId()); // 엔티티에서 작성자 ID 가져옴
    }


    /**
     * 전체 조회
     * */
    @Override
    public List<QaDTO> qaFindAll() {
        return qaBoardRepository.findAll().stream()
                .map(qaBoard -> QaDTO.fromEntity(qaBoard, qaBoard.getManagementUser().getId())) // 엔티티에서 작성자 ID 가져옴
                .toList();
    }


    /**
     * 질문 삭제
     * */
    @Override
    public String qaDelete(Long boardNoSeq) {
        QaBoard qaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        qaBoardRepository.delete(qaBoard);

        return "정상적으로 삭제되었습니다.";
    }

    @Override
    public QaBoard increaseReadnum(Long boardNoSeq) {
        QaBoard qaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        qaBoard.setReadnum(qaBoard.getReadnum() + 1);
        return qaBoardRepository.save(qaBoard);
    }

    /**
     * 현재 로그인한 사용자 ID 가져오기
     */
    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}