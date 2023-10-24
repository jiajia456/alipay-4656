package com.ali.pay;

import com.ali.pay.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class PayApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void MD5T(){

    }

    @Test
    void jwtTest()
    {
         JwtUtils jwtUtils = new JwtUtils();
        String adminjwt = jwtUtils.generateToken("admin","111");
        System.out.println(adminjwt);
        Map<String, Object> usernameFromToken = jwtUtils.getUsernameFromToken(adminjwt);
        System.out.println(usernameFromToken.get("username"));
        System.out.println(usernameFromToken.get("avatar"));

    }

}
