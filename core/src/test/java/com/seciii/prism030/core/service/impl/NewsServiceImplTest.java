package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.core.enums.CategoryType;
import com.seciii.prism030.core.mapper.news.NewsMapperMP;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.pojo.vo.news.NewsItemVO;
import com.seciii.prism030.core.pojo.vo.news.NewsVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Deprecated
@Disabled
class NewsServiceImplTest {
    @MockBean
    private NewsMapperMP newsMapperMPMock;

    @InjectMocks
    private NewsServiceImpl newsService=new NewsServiceImpl(newsMapperMPMock);

    //初始化测试类
    private List<NewsItemVO> fakeNewsItemList;
    private List<NewsPO> fakeNewsPOList;
    private NewsPO fakeNewsPO;
    private NewsVO fakeNewsVO;
    private NewNews fakeNewNews;

    @BeforeEach
    void initTestObjects() {
        Mockito.reset(newsMapperMPMock);
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
        Mockito.when(newsMapperMPMock.insert(Mockito.any(NewsPO.class))).thenReturn(0);
        newsService.addNews(fakeNewNews);
    }

    @Test
    void getNewsDetailTest() {
        Mockito.when(newsMapperMPMock.selectById(512L)).thenReturn(fakeNewsPO);
        NewsVO result = newsService.getNewsDetail(512L);
        assertTrue(newsVOsEqual(result, fakeNewsVO));
    }

    @Test
    void modifyNewsTitleTest() {
        Mockito.when(newsMapperMPMock.selectById(512L)).thenReturn(fakeNewsPO);
        Mockito.when(newsMapperMPMock.updateById(Mockito.any(NewsPO.class))).thenReturn(0);
        newsService.modifyNewsTitle(512L, "newTitle");
    }

    @Test
    void modifyNewsContentTest() {
        Mockito.when(newsMapperMPMock.selectById(512L)).thenReturn(fakeNewsPO);
        Mockito.when(newsMapperMPMock.updateById(Mockito.any(NewsPO.class))).thenReturn(0);
        newsService.modifyNewsContent(512L, "newContent");
    }

    @Test
    void modifyNewsSourceTest() {
        Mockito.when(newsMapperMPMock.selectById(512L)).thenReturn(fakeNewsPO);
        Mockito.when(newsMapperMPMock.updateById(Mockito.any(NewsPO.class))).thenReturn(0);
        newsService.modifyNewsSource(512L, "newSource");
    }

    @Test
    void deleteNewsTest() {
        Mockito.when(newsMapperMPMock.deleteById(512L)).thenReturn(1);
        newsService.deleteNews(512L);
    }

    @Test
    void deleteMultipleNewsTest() {
        Mockito.when(newsMapperMPMock.deleteBatchIds(Mockito.anyList())).thenReturn(0);
        newsService.deleteMultipleNews(List.of(1L, 2L, 3L));

    }

    @Test
    void filterNewsPagedTest() {
        Mockito.when(newsMapperMPMock.getFilteredNewsCount(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn((long) fakeNewsPOList.size());
        Mockito.when(newsMapperMPMock.getFilteredNewsByPage(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(fakeNewsPOList.stream().map(
                x -> {
                    if (x.getTitle().contains("test")) {
                        return x;
                    }
                    return null;
                }
        ).toList());
        List<NewsItemVO> result = newsService.filterNewsPaged(1, 5, null, null, null, null).getNewsList();
        for (int i = 0; i < result.size(); i++) {
            assertTrue(newsItemVOsEqual(result.get(i), fakeNewsItemList.get(i)));
        }
    }

    @Test
    void searchNewsByTitleFilteredTest() {
        Mockito.when(newsMapperMPMock.searchFilteredNewsByPage(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(fakeNewsPOList.stream().map(
                        x -> {
                            if (x.getTitle().contains("test")) {
                                return x;
                            }
                            return null;
                        }
                ).toList());
        Mockito.when(newsMapperMPMock.getSearchedFilteredNewsCount(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn((long) fakeNewsPOList.size());
        List<NewsItemVO> result = newsService.searchNewsByTitleFiltered(1, 5, "test", null, null, null, null).getNewsList();
        for (int i = 0; i < result.size(); i++) {
            assertTrue(newsItemVOsEqual(result.get(i), fakeNewsItemList.get(i)));
        }
    }

}
