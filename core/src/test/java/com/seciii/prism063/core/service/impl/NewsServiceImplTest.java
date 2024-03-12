package com.seciii.prism063.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seciii.prism063.core.enums.CategoryType;
import com.seciii.prism063.core.mapper.NewsMapper;
import com.seciii.prism063.core.pojo.po.NewsPO;
import com.seciii.prism063.core.pojo.vo.news.NewNews;
import com.seciii.prism063.core.pojo.vo.news.NewsItemVO;
import com.seciii.prism063.core.pojo.vo.news.NewsVO;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class NewsServiceImplTest {
    @MockBean
    private NewsMapper newsMapperMock;
    @Autowired
    @InjectMocks
    private NewsServiceImpl newsService;

    //初始化测试类
    private List<NewsItemVO> fakeNewsItemList;
    private List<NewsPO> fakeNewsPOList;
    private NewsPO fakeNewsPO;
    private NewsVO fakeNewsVO;
    private NewNews fakeNewNews;

    @BeforeEach
    void initTestObjects() {
        Mockito.reset(newsMapperMock);
        fakeNewsItemList = new ArrayList<>();
        fakeNewsPOList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LocalDateTime currentTime = LocalDateTime.now();
            fakeNewsItemList.add(NewsItemVO.builder()
                    .id((long) i)
                    .title("test" + i)
                    .originSource("test" + i + "source")
                    .sourceTime(LocalDateTime.parse("2020-03-01 00:01:0" + i, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .category(CategoryType.getCategoryType(i).toString())
                    .build()
            );
            fakeNewsPOList.add(
                    NewsPO.builder()
                            .id((long) i)
                            .title("test" + i)
                            .originSource("test" + i + "source")
                            .sourceTime(LocalDateTime.parse("2020-03-01 00:01:0" + i, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .category(i)
                            .content("testcontent" + i)
                            .link("www.test" + i + ".com")
                            .sourceLink("www.test" + i + "source.com")
                            .createTime(currentTime)
                            .updateTime(currentTime)
                            .build()
            );
        }
        LocalDateTime currentTime = LocalDateTime.now();
        fakeNewsPO = NewsPO.builder()
                .id(512L)
                .title("singularTest")
                .content("singularTestContent")
                .link("www.singulartest.com")
                .sourceLink("www.singulartestsource.com")
                .originSource("singularTestSource")
                .sourceTime(LocalDateTime.parse("2024-01-01 12:34:56", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .category(1)
                .updateTime(currentTime)
                .createTime(currentTime)
                .build();
        fakeNewsVO = NewsVO.builder()
                .id(512L)
                .title("singularTest")
                .content("singularTestContent")
                .link("www.singulartest.com")
                .sourceLink("www.singulartestsource.com")
                .originSource("singularTestSource")
                .sourceTime(LocalDateTime.parse("2024-01-01 12:34:56", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .category(CategoryType.getCategoryType(1).toString())
                .createTime(currentTime)
                .updateTime(currentTime)
                .build();
        fakeNewNews = new NewNews(
                "singularTest",
                "singularTestContent",
                "singularTestSource",
                LocalDateTime.parse("2024-01-01 12:34:56", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "www.singulartest.com",
                "www.singulartestsource.com",
                CategoryType.getCategoryType(1).toString()
        );

    }

    private boolean newsItemVOsEqual(NewsItemVO a, NewsItemVO b) {
        return a.getId().equals(b.getId())
                && a.getTitle().equals(b.getTitle())
                && a.getOriginSource().equals(b.getOriginSource())
                && a.getSourceTime().equals(b.getSourceTime())
                && a.getCategory().equals(b.getCategory());
    }

    private boolean newsVOsEqual(NewsVO a, NewsVO b) {
        return a.getId().equals(b.getId())
                && a.getTitle().equals(b.getTitle())
                && a.getContent().equals(b.getContent())
                && a.getOriginSource().equals(b.getOriginSource())
                && a.getSourceTime().equals(b.getSourceTime())
                && a.getLink().equals(b.getLink())
                && a.getSourceLink().equals(b.getSourceLink())
                && a.getCategory().equals(b.getCategory())
                && a.getCreateTime().equals(b.getCreateTime())
                && a.getUpdateTime().equals(b.getUpdateTime());
    }

    @Test
    void getNewsListTest() {
        Mockito.when(newsMapperMock.selectList(Mockito.any())).thenReturn(fakeNewsPOList);
        Mockito.when(newsMapperMock.selectCount(Mockito.any())).thenReturn((long)fakeNewsPOList.size());
        List<NewsItemVO> result = newsService.getNewsList().getNewsList();
        for (int i = 0; i < result.size(); i++) {
            assertTrue(newsItemVOsEqual(result.get(i), fakeNewsItemList.get(i)));
        }
    }

    @Test
    void addNewsTest() {
        Mockito.when(newsMapperMock.insert(Mockito.any(NewsPO.class))).thenReturn(0);
        newsService.addNews(fakeNewNews);
    }

    @Test
    void getNewsListByPageTest() {
        Mockito.when(newsMapperMock.selectPage(Mockito.any(), Mockito.any()))
                .thenReturn(new Page<NewsPO>(1, 5).setRecords(fakeNewsPOList.subList(0, 5)));
        Mockito.when(newsMapperMock.selectCount(Mockito.any())).thenReturn((long)fakeNewsPOList.size());
        List<NewsItemVO> result = newsService.getNewsListByPage(1, 5).getNewsList();
        for (int i = 0; i < result.size(); i++) {
            assertTrue(newsItemVOsEqual(result.get(i), fakeNewsItemList.get(i)));
        }
    }

    @Test
    void getNewsDetailTest() {
        Mockito.when(newsMapperMock.selectById(512L)).thenReturn(fakeNewsPO);
        NewsVO result = newsService.getNewsDetail(512L);
        assertTrue(newsVOsEqual(result, fakeNewsVO));
    }

    @Test
    void modifyNewsTitleTest() {
        Mockito.when(newsMapperMock.selectById(512L)).thenReturn(fakeNewsPO);
        Mockito.when(newsMapperMock.updateById(Mockito.any(NewsPO.class))).thenReturn(0);
        newsService.modifyNewsTitle(512L, "newTitle");
    }

    @Test
    void modifyNewsContentTest() {
        Mockito.when(newsMapperMock.selectById(512L)).thenReturn(fakeNewsPO);
        Mockito.when(newsMapperMock.updateById(Mockito.any(NewsPO.class))).thenReturn(0);
        newsService.modifyNewsContent(512L, "newContent");
    }

    @Test
    void modifyNewsSourceTest() {
        Mockito.when(newsMapperMock.selectById(512L)).thenReturn(fakeNewsPO);
        Mockito.when(newsMapperMock.updateById(Mockito.any(NewsPO.class))).thenReturn(0);
        newsService.modifyNewsSource(512L, "newSource");
    }

    @Test
    void deleteNewsTest() {
        Mockito.when(newsMapperMock.deleteById(512L)).thenReturn(0);
        newsService.deleteNews(512L);
    }

    @Test
    void filterNewsPagedTest() {
        Mockito.when(newsMapperMock.selectPage(Mockito.any(), Mockito.any()))
                .thenReturn(new Page<NewsPO>(1, 5).setRecords(fakeNewsPOList));
        Mockito.when(newsMapperMock.selectCount(Mockito.any())).thenReturn((long)fakeNewsPOList.size());
        List<NewsItemVO> result = newsService.filterNewsPaged(1, 5, null, null, null).getNewsList();
        for (int i = 0; i < result.size(); i++) {
            assertTrue(newsItemVOsEqual(result.get(i), fakeNewsItemList.get(i)));
        }
    }

    @Test
    void searchNewsByTitleTest() {
        Mockito.when(newsMapperMock.selectList(Mockito.any()))
                .thenReturn(fakeNewsPOList.stream().map(
                        x -> {
                            if (x.getTitle().contains("test")) {
                                return x;
                            }
                            return null;
                        }
                ).toList());
        Mockito.when(newsMapperMock.selectCount(Mockito.any())).thenReturn((long)fakeNewsPOList.size());
        List<NewsItemVO> result = newsService.searchNewsByTitle("test").getNewsList();
        for (int i = 0; i < result.size(); i++) {
            assertTrue(newsItemVOsEqual(result.get(i), fakeNewsItemList.get(i)));
        }
    }


}
