package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.NoticeBoard;
import web.mvc.domain.QaBoard;
import web.mvc.dto.QaDTO;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.ManagementRepository;
import web.mvc.repository.QaBoardRepository;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class QaBoardServiceImpl implements QaBoardService {

    private final QaBoardRepository qaBoardRepository;
    private final ManagementRepository managementRepository;

    /**
     * 질문 등록
     */
    @Override
    public QaDTO qaSave(QaBoard qaBoard) {
        ManagementUser currentUser = getCurrentUser();

        qaBoard.setReadnum(0);
        qaBoard.setFile(null);
        qaBoard.setManagementUser(currentUser);

        QaBoard savedQaBoard = qaBoardRepository.save(qaBoard);
        return convertToDto(savedQaBoard);
    }


    /**
     * 질문 수정
     */
    @Override
    public QaDTO qaUpdate(Long boardNoSeq, QaBoard qaBoard) {

        QaBoard existingQaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        qaBoard.setManagementUser(existingQaBoard.getManagementUser());

        existingQaBoard.setSubject(qaBoard.getSubject());
        existingQaBoard.setContent(qaBoard.getContent());

        QaBoard updatedQaBoard = qaBoardRepository.save(existingQaBoard);
        return convertToDto(updatedQaBoard);
    }


    /**
     * 질문 조회
     */
    @Override
    @Transactional(readOnly = true)
    public QaDTO qaFindBySeq(Long boardNoSeq) {

        QaBoard qaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));
        return convertToDto(qaBoard);
    }


    /**
     * 전체 조회
     */
    @Override
    public List<QaDTO> qaFindAll() {
        List<QaBoard> qaBoards = qaBoardRepository.findAll();
        return qaBoards.stream()
                .map(this::convertToDto)
                .toList();
    }


    /**
     * 질문 삭제
     */
    @Override
    public String qaDelete(Long boardNoSeq) {
        QaBoard qaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        qaBoardRepository.delete(qaBoard);

        return "정상적으로 삭제되었습니다.";
    }


    //조회수 증가
    @Override
    public QaDTO increaseReadnum(Long boardNoSeq) {
        QaBoard qaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        qaBoard.setReadnum(qaBoard.getReadnum() + 1);
        QaBoard updatedQaBoard = qaBoardRepository.save(qaBoard);
        return convertToDto(updatedQaBoard);
    }

    private ManagementUser getCurrentUser() {
        // JWT 토큰에서 현재 사용자 ID 가져오기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        log.info("현재 로그인한 사용자 ID: {}", userId);

        // 데이터베이스에서 사용자 정보 조회
        return managementRepository.findById(userId);

    }

    /**
     * 중복되는 DTO부분을 여기에 메소드 형식으로 만들어 호출시킬 예정
     * */
    private QaDTO convertToDto(QaBoard qaBoard) {
        return QaDTO.builder()
                .boardNoSeq(qaBoard.getBoardNoSeq())
                .subject(qaBoard.getSubject())
                .content(qaBoard.getContent())
                .readnum(qaBoard.getReadnum())
                .regDate(qaBoard.getRegDate())
                .writerId(qaBoard.getManagementUser() != null ? qaBoard.getManagementUser().getId() : null)
                .build();
    }
}



