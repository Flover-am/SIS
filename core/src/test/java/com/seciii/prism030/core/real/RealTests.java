package com.seciii.prism030.core.real;

import com.seciii.prism030.core.pojo.vo.news.NewsWordVO;
import com.seciii.prism030.core.service.impl.NewsServiceMongoImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest

public class RealTests {
    @Autowired
    private NewsServiceMongoImpl newsServiceMongo;
    @Test
    public void getNewsWordCloudToday() {
        List<NewsWordVO> newsWordVOS = newsServiceMongo.getNewsWordCloudToday(10);
        for (NewsWordVO newsWordVO : newsWordVOS) {
            System.out.println(String.format("[%s , %d]", newsWordVO.getText(), newsWordVO.getCount()));
        }
    }
}
