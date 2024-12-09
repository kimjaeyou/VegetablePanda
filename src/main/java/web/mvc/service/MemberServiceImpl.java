package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.*;
import web.mvc.dto.FarmerUserDTO2;
import web.mvc.dto.GetAllUserDTO;
import web.mvc.dto.OrderByBuyCountDTO;
import web.mvc.exception.ErrorCode;
import web.mvc.exception.MemberAuthenticationException;
import web.mvc.repository.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final FarmerUserRepository farmerRepository;
    private final NormalUserRepository normalUserRepository;
    private final CompanyUserRepository companyUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ManagementRepository managementRepository;
    private final WalletRepository walletRepository;
    private final ReviewRepository reviewRepository;
    private final FileRepository fileRepository;
    private final FileService fileService;
    private final S3ImageService s3ImageService;
    private final FarmerMyPageRepository farmerMyPageRepository;

    @Transactional(readOnly = true)
    @Override
    public String duplicateCheck(String id) {
        ManagementUser member = managementRepository.duplicateCheck(id);
        if(member == null) {
            log.info("사용가능한 아이디 입니다.");
            return "사용가능한 아이디 입니다.";
        } else{
            log.info("중복입니다");
            return "아이디가 존재합니다.";
        }
    }

    @Override
    @Transactional
    public void signUp(GetAllUserDTO user, MultipartFile image) {
        log.info("user={}", user);

        if (managementRepository.existsById(user.getId()) > 0) {
            throw new MemberAuthenticationException(ErrorCode.DUPLICATED);
        } else {

            ManagementUser managementUser = new ManagementUser(user.getId(), user.getContent());
            if(image != null) {
                String Image = s3ImageService.upload(image);
                log.info("file생성 - Image : {}", Image);

                // File 객체 생성 및 저장
                File file = new File(Image, user.getId());
                File file2 = fileRepository.save(file);
                managementUser.setFile(file2);
            } else if (image == null) {
                // File 객체 생성 및 저장
                File file = new File(null, user.getId());
                File file2 = fileRepository.save(file);
                managementUser.setFile(file2);
            }
            ManagementUser m = managementRepository.save(managementUser);

            UserWallet userWallet = new UserWallet(0,m.getUserSeq());
            walletRepository.save(userWallet);


            log.info("member = " + m);
            if (user.getContent().equals("farmer")) {
                farmerIn(m, user);
                Review review = new Review(m.getUserSeq(),0,user.getIntro());
                reviewRepository.save(review);
            } else if (user.getContent().equals("user")) {
                userIn(m, user);
            } else if (user.getContent().equals("company")) {
                companyIn(m, user);
            } else {
                throw new MemberAuthenticationException(ErrorCode.WRONG_PASS);
            }
        }
    }

    public void farmerIn(ManagementUser m, GetAllUserDTO user) {
        FarmerUser fuser =
                new FarmerUser(
                        m.getUserSeq(),
                        user.getId(),
                        passwordEncoder.encode(user.getPw()),
                        user.getName(),
                        user.getAddress(),
                        user.getCode(),
                        user.getAcount(),
                        user.getPhone(),
                        user.getEmail(),
                        1,
                        "ROLE_FARMER" // 등급 일단 넣을게 없어서 이거 넣었습니다, 근데 난 등급 별로,,,

                );
        farmerRepository.save(fuser);
    }

    public void userIn(ManagementUser m, GetAllUserDTO user) {
        User uuser =
                new User(
                        m.getUserSeq(),
                        user.getId(),
                        user.getName(),
                        passwordEncoder.encode(user.getPw()),
                        user.getAddress(),
                        user.getGender(),
                        user.getPhone(),
                        user.getEmail(),
                        1,
                        "ROLE_USER"
                );
        normalUserRepository.save(uuser);
    }

    public void companyIn(ManagementUser m, GetAllUserDTO user) {
        CompanyUser cuser =
                new CompanyUser(
                        m.getUserSeq(),
                        user.getId(),
                        user.getComName(),
                        user.getOwnerName(),
                        passwordEncoder.encode(user.getPw()),
                        user.getAddress(),
                        user.getPhone(),
                        user.getCode(),
                        user.getEmail(),
                        user.getRegName(),
                        1,
                        "ROLE_COMPANY"
                );
        companyUserRepository.save(cuser);
    }

    @Override
    public List<FarmerUserDTO2> farmer() {
        return farmerMyPageRepository.farmer();
    }

    @Override
    public List<FarmerUserDTO2> getCombinedFarmerData() {
        // 첫 번째 쿼리 결과 가져오기
        List<FarmerUserDTO2> basicData = farmerMyPageRepository.fetchSortedFarmerData();
        List<OrderByBuyCountDTO> orderRank=farmerMyPageRepository.OrderbyBuyCount();

        return basicData;
    }
}