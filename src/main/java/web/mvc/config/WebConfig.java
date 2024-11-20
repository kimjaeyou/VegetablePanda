package web.mvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로 허용
                .allowedOrigins("http://localhost:5173") // React 앱 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 모든 HTTP 메서드 허용
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true);
    }
}

