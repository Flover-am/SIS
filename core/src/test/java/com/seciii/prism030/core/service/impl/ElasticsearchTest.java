package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.core.enums.CategoryType;
import com.seciii.prism030.core.mapper.news.NewsMapper;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.pojo.vo.news.NewsItemVO;
import com.seciii.prism030.core.pojo.vo.news.NewsVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ElasticsearchTest {

    @Autowired
    private NewsServiceImpl newsService;
    //初始化测试类
    private NewNews fakeNewNews;
    @BeforeEach
    void initTestObjects() {
        fakeNewNews = new NewNews(
                "singularTest",
                "singularTestContent",
                "singularTestSource",
                "2024-01-01 12:34:56",
                "www.singulartest.com",
                "www.singulartestsource.com",
                CategoryType.getCategoryType(1).toString()
        );
    }

    @Test
    void addNewsTest() {
        newsService.addNews(fakeNewNews);
//        newsService.modifyNewsTitle(1L,"test-title1");
    }
}
