package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.File;
import web.mvc.domain.ManagementUser;
import web.mvc.domain.QaBoard;
import web.mvc.dto.FileDTO;
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
    private final FileService fileService;
    private final FileRepository fileRepository;


    /**
     * 게시글 등록
     */
    @Override
    public QaDTO saveQaBoard(QaDTO qaDTO, MultipartFile file) {
        String writerId = getCurrentUserId();

        ManagementUser managementUser = managementRepository.findById(writerId);

        QaBoard qaBoard = qaDTO.toEntity();
        qaBoard.setManagementUser(managementUser);
        qaBoard.setReadnum(0);

        if (file != null && !file.isEmpty()) {
            String uploadedPath = s3ImageService.upload(file);
            File newFile = new File(uploadedPath, file.getOriginalFilename());
            File savedFile = fileService.save(newFile);
            qaBoard.setFile(savedFile);
        }

        QaBoard savedQaBoard = qaBoardRepository.save(qaBoard);
        return QaDTO.fromEntity(savedQaBoard, writerId);
    }
/**
 * 게시글 수정
 * */
    @Override
    public QaDTO qaUpdate(Long boardNoSeq, QaDTO qaDTO, MultipartFile file, boolean deleteFile) {
        QaBoard qaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        qaBoard.setSubject(qaDTO.getSubject());
        qaBoard.setContent(qaDTO.getContent());

        // 파일 처리
        File currentFile = qaBoard.getFile();

        if (deleteFile && currentFile != null) {
            s3ImageService.deleteImageFromS3(currentFile.getPath());
            fileRepository.delete(currentFile);
            qaBoard.setFile(null);
        }

        if (file != null && !file.isEmpty()) {
            // 기존 파일이 있다면 삭제
            if (currentFile != null && !deleteFile) {
                s3ImageService.deleteImageFromS3(currentFile.getPath());
                fileRepository.delete(currentFile);
            }

            String uploadedPath = s3ImageService.upload(file);
            File newFile = fileService.save(new File(uploadedPath, file.getOriginalFilename()));
            qaBoard.setFile(newFile);
        }

        QaBoard updatedQaBoard = qaBoardRepository.save(qaBoard);

        return QaDTO.fromEntity(updatedQaBoard, getCurrentUserId());
    }

    /**
     * 게시글 상세 조회
     */
    @Override
    @Transactional(readOnly = true)
    public QaDTO qaFindBySeq(Long boardNoSeq) {
        QaBoard qaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        // 파일 정보를 DTO로 변환하여 함께 반환
        FileDTO fileDTO = null;
        if (qaBoard.getFile() != null) {
            fileDTO = new FileDTO(
                    qaBoard.getFile().getFileSeq(),
                    qaBoard.getFile().getName(),
                    qaBoard.getFile().getPath()
            );
        }

        QaDTO qaDTO = QaDTO.fromEntity(qaBoard, qaBoard.getManagementUser().getId());
        qaDTO.setFileDTO(fileDTO);
        return qaDTO;
    }

    /**
     * 게시글 목록 조회
     */
    @Override
    public List<QaDTO> qaFindAll() {
        return qaBoardRepository.findAll().stream()
                .map(qaBoard -> QaDTO.fromEntity(qaBoard, qaBoard.getManagementUser().getId()))
                .toList();
    }

    /**
     * 게시글 삭제
     */
    @Override
    public String qaDelete(Long boardNoSeq) {
        QaBoard qaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        // 파일 삭제 처리
        if (qaBoard.getFile() != null) {
            deleteFileFromS3(qaBoard.getFile());
        }

        // 게시글 삭제
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
     * 파일 다운로드
     */
    @Override
    public FileDTO downloadFile(Long boardNoSeq) {
        QaBoard qaBoard = qaBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        if (qaBoard.getFile() == null) {
            throw new DMLException(ErrorCode.FILE_NOTFOUND);
        }

        File file = qaBoard.getFile();
        return new FileDTO(file.getFileSeq(), file.getName(), file.getPath());
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
            log.info("S3 업로드 성공: {}", uploadedPath);
            return new File(uploadedPath, file.getOriginalFilename());
        } catch (Exception e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            throw new DMLException(ErrorCode.UPDATE_FAILED);
        }
    }

    /**
     * S3 파일 삭제
     */
    private void deleteFileFromS3(File file) {
        try {
            s3ImageService.deleteImageFromS3(file.getPath());
            log.info("S3 파일 삭제 성공: {}", file.getPath());
        } catch (Exception e) {
            log.error("파일 삭제 실패: {}", e.getMessage());
            throw new DMLException(ErrorCode.DELETE_FAILED);
        }
    }
}