package web.mvc.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.dto.ShopListDTO;
import web.mvc.repository.ReviewCommentRepository;
import web.mvc.repository.ShopRepository;
import web.mvc.repository.UserBuyRepository;
import web.mvc.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {
    private final ShopRepository shopRepository;
    private final UserBuyRepository buyRepository;
    private final ReviewCommentRepository reviewCommentRepository;

    private List<ShopListDTO> items = new ArrayList<>();

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
        List<ShopListDTO> list=shopRepository.getUserData(user_seq);
        for(ShopListDTO l:list){
            seqList.add(l.getShopSeq());
        }

        return seqList;
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
            System.out.println("!!!!!!!!!!!!!!!!!!!anchor:"+seq);
            fUserSeq=shopRepository.getFarmerUserSeq(seq);
            System.out.println("!!!!!!!!!!!fUserSeq:"+fUserSeq);
            shopList.addAll(shopRepository.getScoreRec(fUserSeq));
            System.out.println("!!!!!!!size :"+shopList.size());
        }
        return shopList;
    }
}
