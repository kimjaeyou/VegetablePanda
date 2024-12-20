package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.FarmerUser;
import web.mvc.dto.*;
import web.mvc.repository.FileRepository;
import web.mvc.service.FarmerMyPageService;
import web.mvc.service.S3ImageService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/myPage/farmer")
public class FarmerMyPageController {

    private final FarmerMyPageService farmerMyPageService;
    private final S3ImageService s3ImageService;
    private final FileRepository fileRepository;
    /**
     * 판매내역
     */
    @GetMapping("/saleList/{seq}")
    public ResponseEntity<List<UserBuyDTO>> saleList(@PathVariable Long seq) {
        List<UserBuyDTO> list = farmerMyPageService.saleList(seq);
        return ResponseEntity.ok(list);
    }

    /**
     * 회원의 정보 조회
     */
    @GetMapping("/list/{seq}")
    public ResponseEntity<?> selectUser(@PathVariable Long seq) {
        return new ResponseEntity<>(farmerMyPageService.selectUser(seq), HttpStatus.OK);
    }

    /**
     * 회원정보 수정
     */
    @PutMapping("/update/{seq}")
    public ResponseEntity<?> update(
            @PathVariable Long seq,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("farmerData") GetAllUserDTO getAllUserDTO) {

        log.info("id={}", getAllUserDTO.getId());
        log.info("image = {}", image);
        log.info("path1 = {}", getAllUserDTO.getPath());

        // 파일 업로드 시작
        if (image != null) { // 이미지 값이 있는상태로 전송되었다면 경우의수를 추적한다.
            String path2 = fileRepository.selectFile(getAllUserDTO.getId());
            log.info("path = {}", path2);
            if (path2 == null) { // 만약 DB에 값이 없으면 바로 업로드 시키고
                String pathUpdate = s3ImageService.upload(image);
                log.info("pathUpdate = {}", pathUpdate);
                fileRepository.updatePath(pathUpdate, getAllUserDTO.getId());
            } else { // DB에 값이 있으면 수정하는거니까 일단 지우고 업로드
                s3ImageService.deleteImageFromS3(path2);
                String pathUpdate = s3ImageService.upload(image);
                log.info("pathUpdate = {}", pathUpdate);
                // String id = managementRepository.findId(seq);
                fileRepository.updatePath(pathUpdate, getAllUserDTO.getId());
            }
        } else if (image == null) { // 근데 이미지값이 없으면 경우의 수 추적
            String file = fileRepository.selectFile(getAllUserDTO.getId()); // 일단 path가 있는지 검색
            String path3 = getAllUserDTO.getPath(); // userData에서 path 받기
            log.info("path3 = {}", path3);
            if (path3 != null) {
                fileRepository.updatePath(path3, getAllUserDTO.getId());
                return new ResponseEntity<>(farmerMyPageService.update(getAllUserDTO, seq), HttpStatus.OK);
            }
            if (file != null) { // 있으면 값 지우기
                String path = fileRepository.selectFile(getAllUserDTO.getId());
                s3ImageService.deleteImageFromS3(path);
                fileRepository.deletePath(getAllUserDTO.getId());
            }
        }
        return new ResponseEntity<>(farmerMyPageService.update(getAllUserDTO, seq), HttpStatus.OK);
    }


    /**
     * 탈퇴
     */
    @PostMapping("/delete/{seq}")
    public int delete(@PathVariable Long seq) {
        return farmerMyPageService.delete(seq);
    }

    /**
     * 정산 내역
     */
    @GetMapping("/point/calc/{seq}")
    public ResponseEntity<?> calcPoint(@PathVariable Long seq) {
        return new ResponseEntity<>(farmerMyPageService.calcPoint(seq), HttpStatus.OK);
    }

    /**
     * 나한테 쓴 리뷰 조회하기
     */
    @GetMapping("/review/List/{seq}")
    public ResponseEntity<?> reviewList(@PathVariable Long seq) {
        System.out.println("\n\n\n"+farmerMyPageService.reviewList(seq).stream().toList());
        return new ResponseEntity<>(farmerMyPageService.reviewList(seq), HttpStatus.OK);
    }

    /**
     * 정산 신청하기
     */
    @PostMapping("/calculate/{seq}")
    public ResponseEntity<String> settle(@PathVariable Long seq, @RequestBody CalculateDTO calculateDTO) {
        try {
            log.info("정산 신청 요청 - userSeq: {}, 데이터: {}", seq, calculateDTO);
            List<CalcPoint2> list = calculateDTO.getCalculateDTO();
            farmerMyPageService.settle(seq, list);
            return ResponseEntity.ok("정산 신청이 완료되었습니다.");
        } catch (Exception e) {
            log.error("정산 신청 처리 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("정산 신청 처리 중 오류가 발생했습니다.");
        }
    }
}