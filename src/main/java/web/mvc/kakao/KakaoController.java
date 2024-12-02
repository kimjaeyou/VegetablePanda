package web.mvc.kakao;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class KakaoController {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    // 카카오 로그인 URL 제공
    @GetMapping("/getKakaoAuthUrl")
    public String getKakaoAuthUrl() {
        return "https://kauth.kakao.com/oauth/authorize" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code";
    }

    // 카카오 로그인 인증 완료 후 콜백 처리
    @GetMapping("/auth")
    public ResponseEntity<?> oauthKakao(@RequestParam("code") String code) {
        String accessToken = getAccessToken(code);
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("카카오 토큰 발급 실패");
        }

        Map<String, Object> userInfo = getUserInfo(accessToken);
        if (userInfo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("카카오 사용자 정보 조회 실패");
        }

        return ResponseEntity.ok(userInfo); // JSON 형태로 사용자 정보 반환
    }

    // Access Token 발급
    private String getAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("client_id", clientId);
        params.put("redirect_uri", redirectUri);
        params.put("code", code);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    tokenUrl, HttpMethod.POST, request, String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonElement element = JsonParser.parseString(response.getBody());
                return element.getAsJsonObject().get("access_token").getAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // 토큰 발급 실패 시 null 반환
    }

    // 사용자 정보 조회
    private Map<String, Object> getUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Bearer 토큰 인증
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    userInfoUrl, HttpMethod.GET, request, String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonElement element = JsonParser.parseString(response.getBody());
                JsonElement properties = element.getAsJsonObject().get("properties");
                JsonElement kakaoAccount = element.getAsJsonObject().get("kakao_account");

                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("nickname", properties.getAsJsonObject().get("nickname").getAsString());
                userInfo.put("email", kakaoAccount.getAsJsonObject().get("email").getAsString());
                userInfo.put("accessToken", accessToken);

                return userInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>(); // 사용자 정보 조회 실패 시 빈 맵 반환
    }
}
