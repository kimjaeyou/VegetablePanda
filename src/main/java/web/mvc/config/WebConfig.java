package web.mvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:5173",
                        "http://www.vegetablepanda.p-e.kr",
                        "http://vegetablepanda.p-e.kr",
                        "https://vegetablepanda.p-e.kr",
                        "https://www.vegetablepanda.p-e.kr"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH")  // OPTIONS 메서드 추가
                .allowedHeaders("*")  // 모든 헤더 허용
                .allowCredentials(true)  // 인증 정보 허용
                .maxAge(3600);  // preflight 캐시 시간
    }
}

