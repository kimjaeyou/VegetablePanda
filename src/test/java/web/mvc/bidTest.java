package web.mvc;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import web.mvc.repository.StockRepository;


//@SpringBootTest 통합 테스트
@Slf4j
@DataJpaTest// 영속성 관련된 테스트
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
//jpa 영속성 context 배울때 plush나 commit때 날라가는데  모아두고 한번에 하는데 롤백될꺼나끼 실행조차 되지않은다.
//시퀀스는 올라간다. 아직 날라가지 않아도 이미 늘려놨기 떄문에
//
public class bidTest {

    @Autowired
    private StockRepository stockRepository;


    @Test
    @DisplayName("재고 넣기")
    public void test() {
    	System.err.println();
    }

}
