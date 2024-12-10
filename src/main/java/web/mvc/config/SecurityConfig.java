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
import web.mvc.jwt.JWTFilter;
import web.mvc.jwt.JWTUtil;
import web.mvc.jwt.LoginFilter;

import java.util.Arrays;
import java.util.Collections;

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
        //CORS 설정
        http.cors((corsCustomizer ->
                corsCustomizer.configurationSource(new CorsConfigurationSource()
                {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Arrays.asList(
                                "https://vegetablepanda.p-e.kr",
                                "https://www.vegetablepanda.p-e.kr",
                                "http://localhost:5173"
                                ));
                        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
                        configuration.setAllowedHeaders(Arrays.asList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setMaxAge(3600L);

                        configuration.addExposedHeader("Set-Cookie");
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                        return configuration;
                    }
                })));

        log.info("Security FilterChain=======================>");
        http.csrf((auth)->auth.disable())
                .formLogin((auth)->auth.disable())
                .httpBasic((auth)->auth.disable());
        //필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함)
        //addFilterAt 은 UsernamePasswordAuthenticationFilter 의 자리에 LoginFilter 가 실행되도록 설정하는 것
        http.addFilterAt(
                new LoginFilter(
                        this.authenticationManager(authenticationConfiguration), jwtUtil),
                UsernamePasswordAuthenticationFilter.class
        );


        // 권한 허용 설정
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/index", "/user", "/user/**", "boards","/api/shop/**","/api/**","/api/cart/**").permitAll()
                .requestMatchers("/members", "/members/**", "/product/**","/stock", "/stock/**", "/api/payment/**","/myPage/**").permitAll()
                .requestMatchers("/swagger-ui", "/swagger-ui/**", "/api/logistics", "/api/swagger-config", "/v3/api-docs/**").permitAll()
                .requestMatchers("http://openapi.seoul.go.kr:8088/**", "/topic/notifications","/charge","/products/**").permitAll()
                .requestMatchers("/ws/**", "/send","/api/streaming/**","/api/statistics/**","/current").permitAll()
                .requestMatchers("/admin","/QABoard","/QABoard/**","/QaReplyBoard","/QaReplyBoard/**","/reviewComment/**").permitAll() //성민 수정
                .anyRequest().authenticated()
        );

        http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        return http.build();
    }

}
