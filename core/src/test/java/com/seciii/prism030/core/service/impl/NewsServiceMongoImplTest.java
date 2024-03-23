package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.core.enums.CategoryType;
import com.seciii.prism030.core.pojo.vo.news.NewsVO;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.utils.DateTimeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Random;

@SpringBootTest
public class NewsServiceMongoImplTest {
    @Autowired
    private NewsServiceMongoImpl newsServiceMongo;
    @Test
    public void testInsert(){
        newsServiceMongo.addNews(new NewNews(
                "testTitle"+ (new Random().nextInt()%20),
                "testContent",
                "testSource",
                DateTimeUtil.defaultFormat(LocalDateTime.now()),
                "testLink",
                "testOriginLink",
                CategoryType.OTHER.getCategoryEN()
        ));
    }
    @Test
    public void testSearch(){
        NewsVO news=newsServiceMongo.getNewsDetail(0L);
        System.out.println(news);
    }
}
