package web.mvc.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import web.mvc.dto.ShopListDTO;
import web.mvc.service.RecommendService;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;
    private List<ShopListDTO> items=new ArrayList<>();

    @PostMapping("recommend/{seq}")
    public ResponseEntity<?> getData(@PathVariable Long seq){
        items=new ArrayList<>();
        int n=0;
        int chk=recommendService.isRec(seq);
        if(chk==0){
            items=null;
            items=recommendService.underRecommend();
        }else if(chk==1){
            items=new ArrayList<>();
            List<Long>seqList= recommendService.shopList(seq);
            Map<String, Object> requestBody = new HashMap<>();
            for(Long seqs:seqList){
                if(n==4) break;
                requestBody.put("shop_seq", seqList.get(Math.toIntExact(n)));
                items.addAll(getShopRecommendations(requestBody));
                n++;
            }
        }else if(chk==2){
            items=new ArrayList<>();
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("user_seq",seq);
            items.addAll(getRecommendations(requestBody));
            System.out.println("컨트롤러 도착 : "+items.size());
        }

        return ResponseEntity.ok(items);
    }

    public List<ShopListDTO> getRecommendations(@RequestBody Map<String, Object> requestBody) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<List> response =
                restTemplate.postForEntity("http://localhost:5000/recommend/py",
                        entity,
                        List.class);

        ObjectMapper mapper = new ObjectMapper();
        List<Long> farmerSeqList = mapper.convertValue(response.getBody(), new TypeReference<List<Long>>() {});
        items=recommendService.overScoreRecommend(farmerSeqList);
        return items;
    }

    public List<ShopListDTO> getShopRecommendations(@RequestBody Map<String, Object> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        List<ShopListDTO> itemList=new ArrayList<>();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Flask 서버 호출
            ResponseEntity<List> response = restTemplate.postForEntity(
                    "http://localhost:5000/recommendShop/py",
                    entity,
                    List.class
            );
            ObjectMapper mapper = new ObjectMapper();
            List<Long> shopSeqList = mapper.convertValue(response.getBody(), new TypeReference<List<Long>>() {});
            itemList=recommendService.overRecommend(shopSeqList);
            if(items!=null){
                return itemList;
            }
            else{
                throw new EmptyStackException();
            }
    }




}
