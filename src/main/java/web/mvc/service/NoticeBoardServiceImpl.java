package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.File;
import web.mvc.domain.NoticeBoard;
import web.mvc.dto.NoticeBoardDTO;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.repository.NoticeBoardRepository;

import java.util.List;
import java.util.stream.Collectors;

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
    public NoticeBoardDTO noticeSave(NoticeBoardDTO noticeBoardDTO, MultipartFile image) {
        log.info("공지사항 등록 요청: {}", noticeBoardDTO);

        try {
            NoticeBoard noticeBoard = NoticeBoard.builder()
                    .subject(noticeBoardDTO.getSubject())
                    .content(noticeBoardDTO.getContent())
                    .readnum(0)
                    .build();

            // 파일 업로드 처리
            if (image != null && !image.isEmpty()) {
                File uploadedFile = uploadFileToS3(image);
                noticeBoard.setFile(uploadedFile);
            }

            NoticeBoard savedNotice = noticeBoardRepository.save(noticeBoard);
            return NoticeBoardDTO.from(savedNotice);
        } catch (Exception e) {
            log.error("공지사항 등록 실패", e);
            throw new DMLException(ErrorCode.SAVE_FAILD);
        }
    }

    /**
     * 공지사항 수정
     */
    @Override
    @Transactional
    public NoticeBoardDTO noticeUpdate(Long boardNoSeq, NoticeBoardDTO noticeBoardDTO,
                                       MultipartFile image, boolean deleteFile) {
        log.info("공지사항 수정 요청: ID={}, 내용={}", boardNoSeq, noticeBoardDTO);

        try {
            NoticeBoard existingNotice = noticeBoardRepository.findById(boardNoSeq)
                    .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

            // 기존 파일 삭제 처리
            if (deleteFile && existingNotice.getFile() != null) {
                deleteFileFromS3(existingNotice.getFile());
                existingNotice.setFile(null);
            }

            // 새 파일 업로드 처리
            if (image != null && !image.isEmpty()) {
                if (existingNotice.getFile() != null) {
                    deleteFileFromS3(existingNotice.getFile());
                }
                File uploadedFile = uploadFileToS3(image);
                existingNotice.setFile(uploadedFile);
            }

            existingNotice.setSubject(noticeBoardDTO.getSubject());
            existingNotice.setContent(noticeBoardDTO.getContent());

            NoticeBoard updatedNotice = noticeBoardRepository.save(existingNotice);
            return NoticeBoardDTO.from(updatedNotice);
        } catch (DMLException e) {
            throw e;
        } catch (Exception e) {
            log.error("공지사항 수정 실패", e);
            throw new DMLException(ErrorCode.UPDATE_FAILED);
        }
    }

    /**
     * 공지사항 단건 조회
     */
    @Override
    public NoticeBoardDTO noticeFindBySeq(Long boardNoSeq) {
        log.info("공지사항 단건 조회 요청: ID={}", boardNoSeq);
        NoticeBoard noticeBoard = noticeBoardRepository.findById(boardNoSeq)
                .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));
        return NoticeBoardDTO.from(noticeBoard);
    }

    /**
     * 공지사항 전체 조회
     */
    @Override
    public List<NoticeBoardDTO> noticeFindAll() {
        log.info("공지사항 전체 조회 요청");
        return noticeBoardRepository.findAll().stream()
                .map(NoticeBoardDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * 공지사항 삭제
     */
    @Override
    @Transactional
    public String noticeDelete(Long boardNoSeq) {
        log.info("공지사항 삭제 요청: ID={}", boardNoSeq);

        try {
            NoticeBoard noticeBoard = noticeBoardRepository.findById(boardNoSeq)
                    .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

            if (noticeBoard.getFile() != null) {
                deleteFileFromS3(noticeBoard.getFile());
            }

            noticeBoardRepository.delete(noticeBoard);
            log.info("공지사항 삭제 성공: ID={}", boardNoSeq);
            return "정상적으로 삭제되었습니다.";
        } catch (DMLException e) {
            throw e;
        } catch (Exception e) {
            log.error("공지사항 삭제 실패", e);
            throw new DMLException(ErrorCode.DELETE_FAILED);
        }
    }

    /**
     * 조회수 증가
     */
    @Override
    @Transactional
    public NoticeBoardDTO increaseReadnum(Long boardNoSeq) {
        try {
            NoticeBoard noticeBoard = noticeBoardRepository.findById(boardNoSeq)
                    .orElseThrow(() -> new DMLException(ErrorCode.NOTFOUND_BOARD));

            noticeBoard.increaseReadnum();
            NoticeBoard updatedNotice = noticeBoardRepository.save(noticeBoard);
            return NoticeBoardDTO.from(updatedNotice);
        } catch (DMLException e) {
            throw e;
        } catch (Exception e) {
            log.error("조회수 증가 실패", e);
            throw new DMLException(ErrorCode.UPDATE_FAILED);
        }
    }

    /**
     * 파일 업로드 처리
     */
    private File uploadFileToS3(MultipartFile image) {
        try {
            String uploadedImagePath = s3ImageService.upload(image);
            log.info("File uploaded to S3 - Path: {}", uploadedImagePath);
            return File.builder()
                    .path(uploadedImagePath)
                    .build();
        } catch (Exception e) {
            log.error("파일 업로드 실패", e);
            throw new DMLException(ErrorCode.UPDATE_FAILED);
        }
    }

    /**
     * S3에서 파일 삭제
     */
    private void deleteFileFromS3(File file) {
        try {
            s3ImageService.deleteImageFromS3(file.getPath());
            log.info("File deleted from S3 - Path: {}", file.getPath());
        } catch (Exception e) {
            log.error("파일 삭제 실패", e);
            throw new DMLException(ErrorCode.DELETE_FAILED);
        }
    }
}
