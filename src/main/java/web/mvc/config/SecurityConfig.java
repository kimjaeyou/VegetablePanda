package web.mvc.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import web.mvc.jwt.JWTFilter;
import web.mvc.jwt.JWTUtil;
import web.mvc.jwt.LoginFilter;

import java.util.Collections;
import java.util.Arrays;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    //AuthenticationManager 가  인자로 받을 AuthenticationConfiguraion 객체 생성자  주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        log.info("authenticationManager==============>");
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        log.info("BCryptPasswordEncoder call=====================>");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CORS 설정 (기존 5173 포트 관련)
        http.cors(corsCustomizer ->
                corsCustomizer.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173")); // 5173 포트 설정
                    configuration.setAllowedMethods(Collections.singletonList("*")); // 모든 HTTP 메서드 허용
                    configuration.setAllowCredentials(true); // 자격 증명 허용
                    configuration.setAllowedHeaders(Collections.singletonList("*")); // 모든 헤더 허용
                    configuration.setMaxAge(3600L); // preflight 요청 캐싱 시간 (초)
                    configuration.setExposedHeaders(Collections.singletonList("Authorization")); // 노출할 헤더
                    return configuration;
                })
        );

        log.info("Security FilterChain=======================>");
        http.csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .formLogin(form -> form.disable()) // Form 기반 인증 비활성화
                .httpBasic(basic -> basic.disable()); // HTTP Basic 인증 비활성화

        // 사용자 정의 필터 추가
        http.addFilterAt(
                new LoginFilter(
                        this.authenticationManager(authenticationConfiguration), jwtUtil
                ),
                UsernamePasswordAuthenticationFilter.class
        );

        // 권한 허용 설정
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/index", "/user", "/user/**", "boards").permitAll()
                .requestMatchers("/members", "/members/**", "/stock", "/stock/**", "/paymnet/**","/api/streaming/**").permitAll()
                .requestMatchers("/swagger-ui", "/swagger-ui/**", "/api/logistics", "/api/swagger-config", "/v3/api-docs/**").permitAll()
                .requestMatchers("http://openapi.seoul.go.kr:8088/**", "/topic/notifications").permitAll()
                .requestMatchers("/ws/**", "/send","/api/streaming/**").permitAll()
                .requestMatchers("/admin").permitAll()
                .anyRequest().authenticated()
        );

        // JWT 필터 추가
        http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        return http.build();
    }

    // 새로운 CORS 설정 추가
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
