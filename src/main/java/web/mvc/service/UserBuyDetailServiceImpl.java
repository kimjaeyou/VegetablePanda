package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.UserBuyDetail;
import web.mvc.dto.UserBuyDetailInfoDTO;
import web.mvc.repository.UserBuyDetailRepository;

import java.util.List;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class UserBuyDetailServiceImpl implements UserBuyDetailService {

    private final UserBuyDetailRepository userBuyDetailRepository;

    @Override
    public List<UserBuyDetail> insertUserBuyDetail(List<UserBuyDetail> userBuyDetails) {
        List<UserBuyDetail> list = userBuyDetailRepository.saveAll(userBuyDetails);
        return list;
    }

    @Override
    public List<UserBuyDetailInfoDTO> getUserBuyDetailInfoes(List<Long> stockSeqs, Long userBuySeq) {
        List<UserBuyDetailInfoDTO> detailInfoDTOs = userBuyDetailRepository.findInfoesByStockSeq(stockSeqs, userBuySeq);
//        if(detailInfoDTOs.size() == 0) { throw new UserBuyException(ErrorCode.ORDER_NOTFOUND); }
        return detailInfoDTOs;
    }
}
