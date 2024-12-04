package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.File;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.QaBoard;
import web.mvc.dto.QaDTO;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.FileRepository;
import web.mvc.repository.ManagementRepository;
import web.mvc.repository.QaBoardRepository;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class QaBoardServiceImpl implements QaBoardService {

    private final QaBoardRepository qaBoardRepository;
    private final S3ImageService s3ImageService;
    private final ManagementRepository managementRepository;
    private final FileRepository fileRepository;

    /**
     * 질문 등록
     */
    @Override
    public QaDTO saveQaBoard(QaBoard qaBoard, MultipartFile file) {
        String writerId = getCurrentUserId();

        // 기존 ManagementUser 검색
        ManagementUser managementUser = managementRepository.findById(writerId);

        // 기본값 설정
        qaBoard.setReadnum(0);
        qaBoard.setManagementUser(managementUser); // 기존 사용자 설정

        // 파일 업로드 처리
        if (file != null && !file.isEmpty()) {
            File uploadedFile = uploadFileToS3(file);

            File savedFile = fileRepository.save(uploadedFile);

            qaBoard.setFile(savedFile);
        }

        // 게시글 저장
        QaBoard savedQaBoard = qaBoardRepository.save(qaBoard);
        return QaDTO.fromEntity(savedQaBoard, writerId);
    }

    /**
     * 질문 수정
     */
    @Override
    public QaDTO qaUpdate(Long boardNoSeq, QaDTO qaDTO, MultipartFile file, boolean deleteFile) {
        QaBoard qa = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        // 제목 및 내용 업데이트
        qa.setSubject(qaDTO.getSubject());
        qa.setContent(qaDTO.getContent());

        // 파일 삭제 요청 처리
        if (deleteFile && qa.getFile() != null) {
            deleteFileFromS3(qa.getFile());
            qa.setFile(null);
        }

        // 새로운 파일 업로드 처리
        if (file != null && !file.isEmpty()) {
            if (qa.getFile() != null) {
                deleteFileFromS3(qa.getFile());
            }
            File uploadedFile = uploadFileToS3(file);
            qa.setFile(uploadedFile);
        }

        // 게시글 업데이트
        QaBoard updatedQaBoard = qaBoardRepository.save(qa);
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

        // 파일 삭제 처리
        if (qaBoard.getFile() != null) {
            deleteFileFromS3(qaBoard.getFile());
        }
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

    /**
     * S3 파일 업로드
     */
    private File uploadFileToS3(MultipartFile file) {
        try {
            String uploadedPath = s3ImageService.upload(file);
            log.info("S3 업로드 파일 패스 : {}", uploadedPath);
            return new File(uploadedPath, file.getOriginalFilename());
        } catch (Exception e) {
            log.error("파일 업로드 실패 : {}", e.getMessage());
            throw new DMLException(ErrorCode.FILE_NOTFOUND);
        }
    }

    /**
     * S3 파일 삭제
     */
    private void deleteFileFromS3(File file) {
        try {
            s3ImageService.deleteImageFromS3(file.getPath());
            log.info("S3 파일 삭제 완료: {}", file.getPath());
        } catch (Exception e) {
            log.error("파일 삭제 실패: {}", e.getMessage());
            throw new DMLException(ErrorCode.FILE_NOTFOUND);
        }
    }
}