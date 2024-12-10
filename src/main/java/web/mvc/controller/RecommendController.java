package web.mvc.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
public class RecommendController {

    private final String flaskServerUrl = "http://localhost:5000/recommend/py";

    @PostMapping("/get-recommendations")
    public List<Map<String, Object>> getRecommendations(@RequestBody Map<String, Object> requestBody) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문에 user_seq 값을 담아서 Flask 서버로 전송
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<List> response = restTemplate.postForEntity(flaskServerUrl, entity, List.class);

        return response.getBody();
    }

    @PostMapping("/get-shop-recommendations")
    public ResponseEntity<?> getShopRecommendations(@RequestBody Map<String, Object> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            System.out.println("!!!!anchor");
            // Flask 서버 호출
            ResponseEntity<List> response = restTemplate.postForEntity(
                    "http://localhost:5000/recommendShop/py",
                    entity,
                    List.class
            );

            // Flask 서버 응답 확인
            System.out.println("!!!!!!! Response: " + response.getBody());

            // 응답 데이터를 반환
            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            // 에러 발생 시 처리
            System.err.println("Error calling Flask server: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch recommendations.");
        }
    }




}
