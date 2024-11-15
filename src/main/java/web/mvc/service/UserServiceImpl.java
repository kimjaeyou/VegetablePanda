package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.*;
import web.mvc.exception.ErrorCode;
import web.mvc.exception.MemberAuthenticationException;
import web.mvc.repository.CompanyRepository;
import web.mvc.repository.FarmerRepository;
import web.mvc.repository.ManagementRepository;
import web.mvc.repository.UserRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final FarmerRepository farmerRepository;
    private final ManagementRepository managementRepository;
    private final PasswordEncoder passoEncoder;
    private final ManagementUser managementUser = new ManagementUser();

    /**
     * 일반 회원가입
     */
    @Transactional
    @Override
    public void userSignUp(User user) {
        log.info("user = {}", user);

        Optional<Boolean> exists = Optional.ofNullable(managementRepository.existsById(user.getId()));

        if (exists.orElse(false)) {
            log.info("아이디 존재함, 다시해야함.");
            throw new MemberAuthenticationException(ErrorCode.DUPLICATED);
        }

        managementUser.setId(user.getId());
        managementUser.setContent("user");

        managementRepository.save(managementUser);
        log.info("managementUser = {}", managementUser);

        if (userRepository.existsById(user.getId())) {
            log.info("가입 실패");
            throw new MemberAuthenticationException(ErrorCode.DUPLICATED);
        }

        user.setUserSeq(managementUser.getUserSeq());
        user.setId(user.getId());
        user.setPw(passoEncoder.encode(user.getPw()));
        user.setName(user.getName());
        user.setEmail(user.getEmail());
        user.setAddress(user.getAddress());
        user.setState(1);
        user.setPhone(user.getPhone());
        user.setGender(user.getGender());
        user.setRole("ROLE_USER");

        log.info("user = {}", user);
        User user1 = userRepository.save(user); // 이렇게 바로 담아버려!!!
    }

    /**
     * 업체 회원가입
     */
    @Transactional
    @Override
    public void companySignUp(CompanyUser companyUser) {
        log.info("companyUser = " + companyUser);

        if (managementRepository.existsById(companyUser.getCompanyId())) {
            log.info("아이디 존재함, 다시해야함.");
            throw new MemberAuthenticationException(ErrorCode.DUPLICATED);
        }
        managementUser.setId(companyUser.getCompanyId());
        managementUser.setContent("company_User");

        managementRepository.save(managementUser);

        // 넣엇으면 이제 각 user 테이블가서 데이터 넣기..
        // 근데 어떻게 구별하지...
        if (companyRepository.existsById(companyUser.getCompanyId())) {
            log.info("가입 실패");
            throw new MemberAuthenticationException(ErrorCode.DUPLICATED);
        }

        // 뭐 성공하면 이렇게 넣으면 되는거 아니겠어?
        companyUser.setUserSeq(managementUser.getUserSeq()); // 이 방법이 뭔가 더 괜찮아 보임. 어차피 시퀀스는 매니지먼트에서 줄꺼니까 그 값만 들고 오면 될거같음.
        companyUser.setCompanyId(companyUser.getCompanyId());
        companyUser.setPw(passoEncoder.encode(companyUser.getPw()));
        companyUser.setComName(companyUser.getComName());
        companyUser.setOwnerName(companyUser.getOwnerName());
        companyUser.setCode(companyUser.getCode());
        companyUser.setAdress(companyUser.getAdress());
        companyUser.setPhone(companyUser.getPhone());
        companyUser.setState(1); // 일단 기본값은 1임. 그러고 나중에 탈퇴하면 0으로 바꿀거임
        companyUser.setRole("ROLE_COMPANY");

        CompanyUser companyUser1 = companyRepository.save(companyUser); // 이렇게 바로 담아버려!!!
        log.info("companyUser1 = {}", companyUser1); // 이거 나오면 뭐 성공이지
    }

    /**
     * 판매자 회원가입
     */
    @Transactional
    @Override
    public void farmerSignUp(FarmerUser farmerUser) {
        log.info("farmerUser = " + farmerUser);

        if (managementRepository.existsById(farmerUser.getFarmerId())) {
            log.info("아이디 존재함, 다시해야함.");
            throw new MemberAuthenticationException(ErrorCode.DUPLICATED);
        }
        managementUser.setId(farmerUser.getFarmerId());
        managementUser.setContent("farmer_User");

        managementRepository.save(managementUser);

        // 넣엇으면 이제 각 user 테이블가서 데이터 넣기..
        // 근데 어떻게 구별하지...
        if (farmerRepository.existsById(farmerUser.getFarmerId())) {
            log.info("가입 실패");
            throw new MemberAuthenticationException(ErrorCode.DUPLICATED);
        }

        // 뭐 성공하면 이렇게 넣으면 되는거 아니겠어?
        farmerUser.setUser_seq(managementUser.getUserSeq()); // 이 방법이 뭔가 더 괜찮아 보임. 어차피 시퀀스는 매니지먼트에서 줄꺼니까 그 값만 들고 오면 될거같음.
        farmerUser.setPw(passoEncoder.encode(farmerUser.getPw()));
        farmerUser.setName(farmerUser.getName());
        farmerUser.setEmail(farmerUser.getEmail());
        farmerUser.setAddress(farmerUser.getAddress());
        farmerUser.setState(1); // 일단 기본값은 1임. 그러고 나중에 탈퇴하면 0으로 바꿀거임
        farmerUser.setPhone(farmerUser.getPhone());
        farmerUser.setCode(farmerUser.getCode());
        farmerUser.setRole("ROLE_USER");
        farmerUser.setAccount("0");

        FarmerUser farmerUser1 = farmerRepository.save(farmerUser); // 이렇게 바로 담아버려!!!
        log.info("farmerUser1 = {}", farmerUser1); // 이거 나오면 뭐 성공이지
    }

    /**
     * 아이디 중복체크
     */
    @Transactional(readOnly = true)
    @Override
    public String duplicateCheck(String userId) {
        User user = userRepository.duplicateCheck(userId);
        System.out.println("user = " + user);
        if (user == null) return "사용가능합니다.";
        else return "중복입니다.";
    }
}

