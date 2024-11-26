package web.mvc.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web.mvc.domain.Bid;
import web.mvc.domain.CompanyUser;
import web.mvc.domain.ReviewComment;
import web.mvc.domain.User;
import web.mvc.dto.CompanyDTO;
import web.mvc.dto.ReviewCommentDTO;
import web.mvc.dto.UserBuyDTO;
import web.mvc.repository.*;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class CompanyMyPageServiceImpl implements CompanyMyPageService {
    private final CompanyMyPageRepository companyMyPageRepository;
    private final CompanyUserRepository companyUserRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원정보 가져오기
     */
    @Override
    public CompanyUser selectUser(Long seq) {
        CompanyUser companyUser = companyMyPageRepository.selectUser(seq);
        return companyUser;
    }

    /**
     * 회원정보 수정
     */
    @Modifying
    @Override
    public CompanyUser update(CompanyUser companyUser, Long seq) {
        String pw = passwordEncoder.encode(companyUser.getPw());
        int no = companyUserRepository.updateUser(
                companyUser.getComName(),
                companyUser.getOwnerName(),
                companyUser.getRegName(),
                companyUser.getEmail(),
                companyUser.getCode(),
                companyUser.getAddress(),
                companyUser.getPhone(),
                pw,
                seq);
        log.info("성공?={}",no);

        CompanyUser companyUser1 = companyUserRepository.findByUserSeq(seq);
        log.info("유저정보={}",companyUser1);

        return companyUser1;
    }
}