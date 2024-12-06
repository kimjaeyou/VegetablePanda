package web.mvc.service;

import web.mvc.domain.UserBuyDetail;

import java.util.List;
import java.util.Optional;

public interface UserBuyDetailService {
    /**
     * 일반 물품 주문 정보 넣기 - 주문 상세품목 (단품 주문)
     */
    public List<UserBuyDetail> insertUserBuyDetail(List<UserBuyDetail> userBuyDetails);


    Optional<UserBuyDetail> findLatestByUserSeq(Long userSeq);
}
