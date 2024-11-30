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
import web.mvc.dto.GetAllUserDTO;
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
    public CompanyDTO selectUser(Long seq) {
        return companyMyPageRepository.selectUser(seq);
    }

    /**
     * 회원정보 수정
     */
    @Modifying
    @Override
    public CompanyUser update(GetAllUserDTO getAllUserDTO, Long seq) {
        String pw = passwordEncoder.encode(getAllUserDTO.getPw());
        String comName = getAllUserDTO.getComName();
        String ownerName = getAllUserDTO.getOwnerName();
        String regName = getAllUserDTO.getRegName();
        String email = getAllUserDTO.getEmail();
        String code = getAllUserDTO.getCode();
        String address = getAllUserDTO.getAddress();
        String phone = getAllUserDTO.getPhone();

        companyUserRepository.updateUser(
                comName,
                ownerName,
                regName,
                email,
                code,
                address,
                phone,
                pw,
                seq
        );

        return companyUserRepository.findByUserSeq(seq);
    }

    @Override
    public int delete(Long seq) {
        int i = companyUserRepository.delete(seq);
        log.info("i = {}", i);
        return i;
    }
}