package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.core.classifier.Classifier;
import com.seciii.prism030.core.dao.news.impl.NewsDAOMongoImpl;
import com.seciii.prism030.core.enums.CategoryType;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.vo.news.ClassifyResultVO;
import com.seciii.prism030.core.pojo.vo.news.NewsItemVO;
import com.seciii.prism030.core.pojo.vo.news.NewsVO;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class NewsServiceMongoImplTest {
    @MockBean
    private NewsDAOMongoImpl newsDAOMongoMock;
    @MockBean
    private Classifier classifier;
    @MockBean
    private RedisService redisService;

    @InjectMocks
    private NewsServiceMongoImpl newsServiceMongoImpl = new NewsServiceMongoImpl();

    private List<NewsItemVO> fakeNewsItemList;
    private List<NewsPO> fakeNewsPOList;
    private NewsPO fakeNewsPO;
    private NewsVO fakeNewsVO;
    private NewNews fakeNewNews;
    private List<Pair<CategoryType, Double>> fakeClassifyResult;

    @BeforeEach
    void initTestObjects() {
        Mockito.reset(newsDAOMongoMock);
        fakeNewsItemList = new ArrayList<>();
        fakeNewsPOList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LocalDateTime currentTime = LocalDateTime.now();
            fakeNewsItemList.add(NewsItemVO.builder()
                    .id((long) i)
                    .title("test" + i)
                    .originSource("test" + i + "source")
                    .sourceTime("2020-03-01 00:01:0" + i)
                    .updateTime("2022-03-01 00:01:01")
                    .link("selearning.nju.edu.cn")
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
                .sourceTime("2024-01-01 12:34:56")
                .category(CategoryType.getCategoryType(1).toString())
                .createTime(currentTime)
                .updateTime(currentTime)
                .build();
        fakeNewNews = new NewNews(
                "singularTest",
                "singularTestContent",
                "singularTestSource",
                "2024-01-01 12:34:56",
                "www.singulartest.com",
                "www.singulartestsource.com",
                CategoryType.getCategoryType(1).toString()
        );
        fakeClassifyResult = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            fakeClassifyResult.add(Pair.of(CategoryType.getCategoryType(i), 0.1 * i));
        }
        Mockito.when(redisService.countCategoryNews(Mockito.anyInt(), Mockito.any())).thenReturn(1);
        Mockito.when(redisService.countDateNews()).thenReturn(1);
        Mockito.when(redisService.countDateNews(Mockito.any())).thenReturn(3);


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
    void addNewsTest() {
        Mockito.when(newsDAOMongoMock.insert(Mockito.any())).thenReturn(fakeNewsPO);
        newsServiceMongoImpl.addNews(fakeNewNews);
    }

    @Test
    void getNewsDetailTest() {
        Mockito.when(newsDAOMongoMock.getNewsById(Mockito.anyLong())).thenReturn(fakeNewsPO);
        NewsVO result = newsServiceMongoImpl.getNewsDetail(512L);
        assert newsVOsEqual(result, fakeNewsVO);
    }

    @Test
    void modifyNewsTest() {
        Mockito.when(newsDAOMongoMock.updateNewsContent(Mockito.anyLong(), Mockito.anyString())).thenReturn(0);
        Mockito.when(newsDAOMongoMock.updateNewsTitle(Mockito.anyLong(), Mockito.anyString())).thenReturn(0);
        Mockito.when(newsDAOMongoMock.updateNewsSource(Mockito.anyLong(), Mockito.anyString())).thenReturn(0);
        newsServiceMongoImpl.modifyNewsContent(512L, "newContent");
        newsServiceMongoImpl.modifyNewsTitle(512L, "newTitle");
        newsServiceMongoImpl.modifyNewsSource(512L, "newSource");
    }

    @Test
    void deleteNewsTest() {
        Mockito.when(newsDAOMongoMock.deleteById(Mockito.any())).thenReturn(0);
        newsServiceMongoImpl.deleteNews(512L);
    }

    @Test
    void deleteMultipleNewsTest() {
        List<Long> idList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            idList.add((long) i);
        }
        Mockito.when(newsDAOMongoMock.batchDeleteNews(Mockito.any())).thenReturn((long) idList.size());
        newsServiceMongoImpl.deleteMultipleNews(idList);
    }

    @Test
    void filterNewsPagedTest() {
        Mockito.when(newsDAOMongoMock.getFilteredNewsCount(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn((long) fakeNewsPOList.size());
        Mockito.when(newsDAOMongoMock.getFilteredNewsByPage(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(fakeNewsPOList.stream().map(
                x -> {
                    if (x.getTitle().contains("test")) {
                        return x;
                    }
                    return null;
                }
        ).toList());
        List<NewsItemVO> result = newsServiceMongoImpl.filterNewsPaged(1, 5, null, null, null, null).getNewsList();
        for (int i = 0; i < result.size(); i++) {
            assertTrue(newsItemVOsEqual(result.get(i), fakeNewsItemList.get(i)));
        }
    }

    @Test
    void searchNewsByTitleFilteredTest() {
        Mockito.when(newsDAOMongoMock.searchFilteredNewsByPage(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(fakeNewsPOList.stream().map(
                        x -> {
                            if (x.getTitle().contains("test")) {
                                return x;
                            }
                            return null;
                        }
                ).toList());
        Mockito.when(newsDAOMongoMock.getSearchedFilteredNewsCount(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn((long) fakeNewsPOList.size());
        List<NewsItemVO> result = newsServiceMongoImpl.searchNewsByTitleFiltered(1, 5, "test", null, null, null, null).getNewsList();
        for (int i = 0; i < result.size(); i++) {
            assertTrue(newsItemVOsEqual(result.get(i), fakeNewsItemList.get(i)));
        }
    }

    @Test
    void topNClassifyTest() {
        Mockito.when(classifier.topNClassify(Mockito.anyString(), Mockito.anyInt())).thenReturn(fakeClassifyResult);
        List<ClassifyResultVO> result = newsServiceMongoImpl.topNClassify("test", 5);
        for (int i = 0; i < 5; i++) {
            assertTrue(CategoryType.of(i).equals(result.get(i).getCategory()));
            assertEquals(0.1 * i, result.get(i).getProbability());
        }
    }

    @Test
    void countPeriod() {
        assertDoesNotThrow(() -> newsServiceMongoImpl.countPeriodNews("2022-01-01", "2022-01-03"));
        assertEquals(0, newsServiceMongoImpl.countPeriodNews("2022-01-04", "2022-01-03").size());
    }

    @Test
    void c() {
        assertDoesNotThrow(() -> newsServiceMongoImpl.countAllCategoryNews());
    }
}
