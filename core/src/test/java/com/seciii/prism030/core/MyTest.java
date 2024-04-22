package com.seciii.prism030.core;

import com.seciii.prism030.core.service.impl.SummaryServiceRedisImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyTest {
    @Autowired
    private SummaryServiceRedisImpl summaryServiceRedis;

    @Test
    public void test() {
        System.out.println(summaryServiceRedis.diffTodayAndYesterday());
    }
}
