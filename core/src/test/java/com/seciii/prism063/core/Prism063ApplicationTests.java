package com.seciii.prism063.core;

import com.seciii.prism063.core.mapper.auth.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Prism063ApplicationTests {
    @Autowired
    UserMapper userMapper;
    @Test
    void contextLoads() {
    }

    @Test
    void test() {
        int i = userMapper.existsUser("王liu");
        System.out.println(i);
    }

}
