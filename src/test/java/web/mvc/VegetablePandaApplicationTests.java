package web.mvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.User;
import web.mvc.repository.UserRepository;

@SpringBootTest
@Transactional
@Commit
class VegetablePandaApplicationTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    void memberInsert() {

    }

}
