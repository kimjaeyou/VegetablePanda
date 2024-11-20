package web.mvc.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.siot.IamportRestClient.IamportClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;

@Configuration // 환경설정을 돕는 클래스, 서버가 start 될때 @Configuration안에 있는 설정이 세팅된다.
@Slf4j
public class AppConfig {

    @Value("${imp.api.key}")
    private String apikey;

    @Value("${imp.api.secretkey}")
    private String secretKey;

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apikey, secretKey);
    }

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

}
