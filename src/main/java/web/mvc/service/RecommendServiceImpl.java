package web.mvc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.dto.GetProData;
import web.mvc.dto.ShopListDTO;
import web.mvc.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

    private final ShopRepository shopRepository;
    private final UserBuyRepository buyRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final StockRepository stockRepository;

    private List<ShopListDTO> items = new ArrayList<>();

    @Override
    public GetProData getProData(Long stockSeq) {
        List<Object[]> result = stockRepository.findGetProData(stockSeq);
        if (result.isEmpty()) {
            throw new EntityNotFoundException("Data not found");
        }
        Object[] row = result.get(0);
        return new GetProData((Long)row[0], (String) row[1], (Integer) row[2], (Integer) row[3], (String) row[4], (String) row[5],(String) row[6]);
    }

    @Override
    public int isRec(long user_seq){
        int n=0;
        int isChkBuy=buyRepository.isRec(user_seq);
        int isChkReview=reviewCommentRepository.isRecReview(user_seq);
        if(isChkBuy<2){//상위 판매 추천
        }else if(isChkBuy>=2 && isChkReview>=2){//협력 추천
            n=2;
        }else if(isChkBuy>=2 && isChkReview<2) {//컨텐츠기반 추천
            n = 1;
        }

        return n;
    }

    @Override
    public List<Long> shopList(long user_seq) {
        List<Long> seqList = new ArrayList<>();
        List<Long> list=shopRepository.getUserData(user_seq);
        return list;
    }

    @Override
    public List<ShopListDTO> underRecommend() {
     return shopRepository.getShopRecommendations();
    }

    @Override
    public List<ShopListDTO> overRecommend(List<Long> seqList) {
        List<ShopListDTO> shopList = new ArrayList<>();

        for(Long seq:seqList){
            shopList.add(shopRepository.getShopItemRecommendations(seq));
        }

        return shopList;
    }

    @Override
    public List<ShopListDTO> overScoreRecommend(List<Long> seqList) {
        List<ShopListDTO> shopList = new ArrayList<>();
        long fUserSeq;
        for(Long seq:seqList){
            fUserSeq=shopRepository.getFarmerUserSeq(seq);
            shopList.addAll(shopRepository.getScoreRec(fUserSeq));
        }
        return shopList;
    }


}
