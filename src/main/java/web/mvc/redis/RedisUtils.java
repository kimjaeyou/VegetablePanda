package web.mvc.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class RedisUtils {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        log.info("redis util template 실행");
        this.redisTemplate = redisTemplate;
    }

    // 낙관적 잠금 시 사용할 watch 메서드
    public void watch(String key) {
        redisTemplate.watch(key);
    }

    // 트랜잭션 시작
    public void multi() {
        redisTemplate.multi();
    }

    // 트랜잭션 실행
    public void exec() {
        redisTemplate.exec();
    }

    // 트랜잭션 취소
    public void discard() {
        redisTemplate.discard();
    }

    public <T> boolean saveData(String key, T data) {
        try {
            System.out.println(key);
            ObjectMapper mapper = new ObjectMapper();
            String value = mapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch(Exception e){
            log.error(e.toString());
            return false;
        }
    }

    public <T> Optional<T> getData(String key, Class<T> classType) {
        String value = (String) redisTemplate.opsForValue().get(key);

        if (value == null) {
            return Optional.empty();
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            return Optional.of(mapper.readValue(value, classType));
        } catch (Exception e) {
            log.error(e.toString());
            return Optional.empty();
        }
    }

    // Hash로 데이터를 저장하는 메서드
    public <T> boolean saveHashData(String key, String hashKey, T data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String value = mapper.writeValueAsString(data);

            // Hash에 데이터를 저장
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            log.error("Error saving data to Redis", e);
            return false;
        }
    }

    // Hash에서 데이터를 가져오는 메서드
    public <T> Optional<T> getHashData(String key, String hashKey, Class<T> classType) {
        String value = (String) redisTemplate.opsForHash().get(key, hashKey);

        if (value == null) {
            return Optional.empty();
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            return Optional.of(mapper.readValue(value, classType));
        } catch (Exception e) {
            log.error("Error retrieving data from Redis", e);
            return Optional.empty();
        }
    }
    public void deleteData(String key){
        redisTemplate.delete(key);
    }
}