package web.mvc.service;

import web.mvc.dto.ShopListDTO;

import java.util.List;

public interface RecommendService {
    //모델 판별 체크
    int isRec(long user_seq);
    //
    List<Long> shopList(long user_seq);
    //3건 이하 구매 고객
    List<ShopListDTO> underRecommend();
    //3건 이상 구매 고객 but 리뷰 2건 이상 평가x
    List<ShopListDTO> overRecommend(List<Long> seqList);
    //3건 이상 구매 고객 and 리뷰 2건 이상 평가o
    List<ShopListDTO> overScoreRecommend(List<Long> seqList);
}
