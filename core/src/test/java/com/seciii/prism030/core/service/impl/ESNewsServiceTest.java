package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.common.exception.NewsException;
import com.seciii.prism030.common.exception.error.ErrorType;
import com.seciii.prism030.core.enums.CategoryType;
import com.seciii.prism030.core.pojo.po.es.ESNewsPO;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.pojo.vo.news.NewsVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ESNewsServiceTest {

    @Autowired
    private NewsServiceImpl newsService;
    @Autowired
    private ESNewsServiceImpl esNewsService;
    //初始化测试类
    private NewNews fakeNewNews;
    @BeforeEach
    void initTestObjects() {
        fakeNewNews = new NewNews(
                "singular test",
                "singularTestContent",
                "singularTestSource",
                "2024-01-01 12:34:56",
                "www.singulartest.com",
                "www.singulartestsource.com",
                CategoryType.getCategoryType(1).toString()
        );
    }

    @Test
    void addESNewsTest() {
        long newsId = newsService.addNews(fakeNewNews);
        esNewsService.addESNews(fakeNewNews,newsId);
        NewsVO newsVO = esNewsService.getESNewsDetailByNewsId(newsId);
        NewsVO newsVO1 = newsService.getNewsDetail(newsId);
        assertEquals(newsVO.getId(),newsVO1.getId());
    }
    @Test
    void deleteESNewsTest() {
        long newsId = newsService.addNews(fakeNewNews);
        esNewsService.addESNews(fakeNewNews,newsId);
        newsService.deleteNews(newsId);
        esNewsService.deleteESNewsByNewsId(newsId);

        NewsException exception = assertThrows(NewsException.class, () -> {
            esNewsService.deleteESNewsByNewsId(newsId);
        });
        assertEquals(ErrorType.NEWS_NOT_FOUND, exception.getErrorType());

        NewsException exception1 = assertThrows(NewsException.class, () -> {
            newsService.deleteNews(newsId);
        });
        assertEquals(ErrorType.NEWS_NOT_FOUND, exception1.getErrorType());
    }

    @Test
    void getESNewsByNewsIdTest() {
        long newsId = 8L;
        NewsVO newsVO = esNewsService.getESNewsDetailByNewsId(newsId);
        NewsVO newsVO1 = newsService.getNewsDetail(newsId);
        assertEquals(newsVO.getId(),newsVO1.getId());
    }
    @Test
    void modifyESNewsTitleTest() {
        long newsId = newsService.addNews(fakeNewNews);
        esNewsService.addESNews(fakeNewNews,newsId);
        String title = "这是个测试标题";
        esNewsService.modifyESNewsTitle(newsId,title);
        NewsVO newsVO = esNewsService.getESNewsDetailByNewsId(newsId);
        assertEquals(newsVO.getTitle(),title);
    }
    @Test
    void modifyESNewsContentTest() {
        long newsId = newsService.addNews(fakeNewNews);
        esNewsService.addESNews(fakeNewNews,newsId);
        String content = "这是个测试内容";
        esNewsService.modifyESNewsContent(newsId,content);
        NewsVO newsVO = esNewsService.getESNewsDetailByNewsId(newsId);
        assertEquals(newsVO.getContent(),content);
    }

    @Test
    void modifyESNewsSourceTest() {
        long newsId = newsService.addNews(fakeNewNews);
        esNewsService.addESNews(fakeNewNews,newsId);
        String source = "这是个测试来源";
        esNewsService.modifyESNewsSource(newsId,source);
        NewsVO newsVO = esNewsService.getESNewsDetailByNewsId(newsId);
        assertEquals(newsVO.getOriginSource(),source);
    }

    @Test
    void fuzzySearchByTitleTest() {
        long newsId = newsService.addNews(fakeNewNews);
        esNewsService.addESNews(fakeNewNews,newsId);
        NewsVO newsVO = esNewsService.getESNewsDetailByNewsId(newsId);
        List<ESNewsPO> esNewsPOS = esNewsService.fuzzySearchByTitle("tesx");
        for(ESNewsPO esNewsPO : esNewsPOS){
            assertEquals(newsVO.getTitle(),esNewsPO.getTitle());
            System.out.println(esNewsPO.getTitle());
        }
    }

}
