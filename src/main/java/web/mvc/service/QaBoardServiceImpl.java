package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.File;
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
    private final S3ImageService s3ImageService;

    /**
     * 질문 등록
     */
    @Override
    public QaDTO qaSave(QaDTO qaDTO, MultipartFile image) {
        String writerId = getCurrentUserId();

        // DTO -> Entity 변환
        QaBoard qaBoard = qaDTO.toEntity();
        qaBoard.setReadnum(0);
        qaBoard.getManagementUser().setId(writerId); // 작성자 설정

        // 이미지 업로드 처리
        if (image != null && !image.isEmpty()) {
            qaBoard.setFile(uploadFileToS3(image));
        }

        QaBoard savedQaBoard = qaBoardRepository.save(qaBoard);
        return QaDTO.fromEntity(savedQaBoard, writerId);
    }

    /**
     * 질문 수정
     */
    @Override
    public QaDTO qaUpdate(Long boardNoSeq, QaDTO qaDTO, MultipartFile image, boolean deleteFile) {
        QaBoard existingQa = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        // 이미지 처리
        if (deleteFile && existingQa.getFile() != null) {
            deleteFileFromS3(existingQa.getFile());
            existingQa.setFile(null);
        }
        if (image != null && !image.isEmpty()) {
            if (existingQa.getFile() != null) {
                deleteFileFromS3(existingQa.getFile());
            }
            existingQa.setFile(uploadFileToS3(image));
        }

        // 게시글 내용 수정
        existingQa.setSubject(qaDTO.getSubject());
        existingQa.setContent(qaDTO.getContent());

        QaBoard updatedQaBoard = qaBoardRepository.save(existingQa);
        return QaDTO.fromEntity(updatedQaBoard, updatedQaBoard.getManagementUser().getId());
    }

    /**
     * 질문 조회
     */
    @Override
    @Transactional(readOnly = true)
    public QaDTO qaFindBySeq(Long boardNoSeq) {
        QaBoard qaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));
        return QaDTO.fromEntity(qaBoard, qaBoard.getManagementUser().getId());
    }

    /**
     * 전체 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<QaDTO> qaFindAll() {
        return qaBoardRepository.findAll().stream()
                .map(qaBoard -> QaDTO.fromEntity(qaBoard, qaBoard.getManagementUser().getId()))
                .toList();
    }

    /**
     * 질문 삭제
     */
    @Override
    public String qaDelete(Long boardNoSeq) {
        QaBoard qaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        // 연관된 이미지 삭제
        if (qaBoard.getFile() != null) {
            deleteFileFromS3(qaBoard.getFile());
        }

        qaBoardRepository.delete(qaBoard);

        return "정상적으로 삭제되었습니다.";
    }

    /**
     * 조회수 증가
     */
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

    /**
     * 파일 업로드 처리
     */
    private File uploadFileToS3(MultipartFile image) {
        String uploadedPath = s3ImageService.upload(image);
        log.info("File uploaded successfully: {}", uploadedPath);
        return new File(uploadedPath, image.getOriginalFilename());
    }

    /**
     * S3에서 파일 삭제
     */
    private void deleteFileFromS3(File file) {
        s3ImageService.deleteImageFromS3(file.getPath());
        log.info("File deleted successfully: {}", file.getPath());
    }
}