package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.File;
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
    private final S3ImageService s3ImageService;

    /**
     * 공지사항 등록
     */
    @Override
    @Transactional
    public NoticeBoard noticeSave(NoticeBoard noticeBoard, MultipartFile image) {
        log.info("공지사항 등록 요청: {}", noticeBoard);

        // 파일 업로드 처리
        if (image != null && !image.isEmpty()) {
            File uploadedFile = uploadFileToS3(image);
            noticeBoard.setFile(uploadedFile);
        }

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
    public NoticeBoard noticeUpdate(Long boardNoSeq, NoticeBoard noticeBoard, MultipartFile image, boolean deleteFile) {
        log.info("공지사항 수정 요청: ID={}, 내용={}", boardNoSeq, noticeBoard);

        NoticeBoard existingNotice = noticeBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.UPDATE_FAILED));

        // 기존 파일 삭제 처리
        if (deleteFile && existingNotice.getFile() != null) {
            deleteFileFromS3(existingNotice.getFile());
            existingNotice.setFile(null);
        }

        // 새 파일 업로드 처리
        if (image != null && !image.isEmpty()) {
            File uploadedFile = uploadFileToS3(image);
            existingNotice.setFile(uploadedFile);
        }

        // 공지사항 내용 수정
        existingNotice.setSubject(noticeBoard.getSubject());
        existingNotice.setContent(noticeBoard.getContent());

        return noticeBoardRepository.save(existingNotice);
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
        log.info("공지사항 삭제 요청: ID={}", boardNoSeq);

        NoticeBoard noticeBoard = noticeBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

        // 파일 삭제 처리
        if (noticeBoard.getFile() != null) {
            deleteFileFromS3(noticeBoard.getFile());
        }

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


    /**
     * 파일 업로드 처리
     */
    private File uploadFileToS3(MultipartFile image) {
        String uploadedImagePath = s3ImageService.upload(image);
        log.info("File uploaded to S3 - Path: {}", uploadedImagePath);
        return new File(uploadedImagePath, image.getOriginalFilename());
    }

    /**
     * S3에서 파일 삭제
     */
    private void deleteFileFromS3(File file) {
        s3ImageService.deleteImageFromS3(file.getPath());
        log.info("File deleted from S3 - Path: {}", file.getPath());
    }
}
