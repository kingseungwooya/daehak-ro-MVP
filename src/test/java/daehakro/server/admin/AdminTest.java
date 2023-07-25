package daehakro.server.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class AdminTest {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void 관리자_비밀번호() {
        String adminPW = "test";
        System.out.println(bCryptPasswordEncoder.encode(adminPW));
    }
}
