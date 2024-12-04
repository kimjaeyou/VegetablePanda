package web.mvc.service;

import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.QaBoard;
import web.mvc.dto.QaDTO;

import java.util.List;

public interface QaBoardService {

    /**
     * QA 등록
     * */
    public QaDTO saveQaBoard(QaBoard qaBoard, MultipartFile file);

    /**
     * QA 수정
     */
    public QaDTO qaUpdate(Long boardNoSeq, QaDTO qaDTO, MultipartFile file, boolean deleteFile);

    /**
     * QA 조회
     */
    public QaDTO qaFindBySeq(Long boardNoSeq);

    /**
     * 전체 조회
     */
    public List<QaDTO> qaFindAll();

    /**
     * QA 삭제
     * */
    public String qaDelete(Long boardNoSeq);

    QaBoard increaseReadnum(Long boardNoSeq);

}
