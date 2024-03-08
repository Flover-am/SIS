package com.seciii.prism063.core.controller;


import com.seciii.prism063.common.Result;
import com.seciii.prism063.core.pojo.vo.news.NewsItemVO;
import com.seciii.prism063.core.pojo.vo.news.NewsVO;
import com.seciii.prism063.core.service.NewsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class NewsControllerTests {
    @Mock
    private NewsService newsServiceMock;
    @InjectMocks
    private NewsController newsController;
    private List<NewsItemVO> fakeNewsList;
    private NewsVO fakeNewsVO;
    @Before
    public void before(){
        fakeNewsList=new ArrayList<>();
        for(int i=0;i<10;i++){
            fakeNewsList.add(NewsItemVO.builder()
                    .id((long)i)
                    .title("test"+i)
                    .origin_source("test"+i+"source")
                    .source_time(LocalDateTime.parse("2024-03-04 00:01:0"+i, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .category("test"+i+"cat")
                    .build()
            );
        }
        fakeNewsVO=NewsVO.builder()
                .id(0L)
                .title("test")
                .content("testcontent")
                .origin_source("testsource")
                .source_time(LocalDateTime.parse("2024-03-04 00:01:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .link("www.test.com")
                .source_link("www.testsource.com")
                .category("testcat")
                .build();
    }
    @Test
    public void getNewsListMockTest(){
        Mockito.when(newsServiceMock.getNewsList()).thenReturn(fakeNewsList);
        Result<List<NewsItemVO>> result = newsController.getNewsList();
        Assert.assertEquals((Integer)0,result.getCode());
        Assert.assertEquals("success",result.getMsg());
        Assert.assertArrayEquals(fakeNewsList.toArray(),result.getData().toArray());
    }
    @Test
    public void addNewsMockTest(){
        Result<Void> result = newsController.addNews(null);
        Assert.assertEquals((Integer)0,result.getCode());
        Assert.assertEquals("success",result.getMsg());
    }
    @Test
    public void getNewsListByPageMockTest(){
        Mockito.when(newsServiceMock.getNewsListByPage(1,5)).thenReturn(fakeNewsList.subList(0,5));
        Result<List<NewsItemVO>> result = newsController.getNewsListByPage(1,5);
        Assert.assertEquals((Integer)0,result.getCode());
        Assert.assertEquals("success",result.getMsg());
        Assert.assertArrayEquals(fakeNewsList.subList(0,5).toArray(),result.getData().toArray());
    }
    @Test
    public void getNewsDetailMockTest(){
        Mockito.when(newsServiceMock.getNewsDetail(0L)).thenReturn(fakeNewsVO);
        Result<NewsVO> result = newsController.getNewsDetail(0L);
        Assert.assertEquals((Integer)0,result.getCode());
        Assert.assertEquals("success",result.getMsg());
        Assert.assertEquals(fakeNewsVO,result.getData());
    }



}
