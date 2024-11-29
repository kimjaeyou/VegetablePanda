package web.mvc.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
}
