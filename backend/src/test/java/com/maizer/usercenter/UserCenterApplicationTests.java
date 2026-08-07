package com.maizer.usercenter;

import com.maizer.usercenter.utils.PasswordUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@SpringBootTest
class UserCenterApplicationTests {

    @Test
    void testDigest()  {
        String s = PasswordUtil.encryptPassword("userPassword");
        System.out.println(s);
    }
}
