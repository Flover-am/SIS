package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.common.exception.NewsException;
import com.seciii.prism030.core.decorator.classifier.Classifier;
import com.seciii.prism030.core.dao.news.impl.NewsDAOMongoImpl;
import com.seciii.prism030.core.decorator.segment.TextSegment;
import com.seciii.prism030.core.enums.CategoryType;
import com.seciii.prism030.core.enums.SpeechPart;
import com.seciii.prism030.core.event.publisher.UpdateNewsPublisher;
import com.seciii.prism030.core.mapper.news.VectorNewsMapper;
import com.seciii.prism030.core.pojo.dto.NewsWordDetail;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.po.news.NewsSegmentPO;
import com.seciii.prism030.core.pojo.po.news.NewsWordPO;
import com.seciii.prism030.core.pojo.vo.news.*;
import com.seciii.prism030.core.service.SummaryService;
import com.seciii.prism030.core.utils.DateTimeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ComponentScan(
        basePackages = {
                "com.seciii.prism030.core.aspect",
                "com.seciii.prism030.core.aspect.persistence",
                "com.seciii.prism030.core.config"
        })
public class NewsServiceMongoImplTest {
    @MockBean
    private NewsDAOMongoImpl newsDAOMongoMock;
    @MockBean
    private Classifier classifierMock;
    @MockBean
    private TextSegment textSegmentMock;
    @MockBean
    private SummaryService summaryServiceMock;

    @InjectMocks
    private NewsServiceMongoImpl newsServiceMongoImpl = new NewsServiceMongoImpl();
    @MockBean
    private VectorNewsMapper vectorNewsMapper;
    private List<NewsItemVO> fakeNewsItemList;
    private List<NewsPO> fakeNewsPOList;
    private NewsPO fakeNewsPO;
    private NewsVO fakeNewsVO;
    private NewNews fakeNewNews;
    private List<Pair<CategoryType, Double>> fakeClassifyResult;

    private List<NewsSourceCountVO> allSourceNewsList;

    @BeforeEach
    void initTestObjects() {
        Mockito.when(vectorNewsMapper.deleteVectorNewsByNewsId(Mockito.anyLong())).thenReturn(1);
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
                            .sourceTime(DateTimeUtil.toMongoStandardFormat(LocalDateTime.parse("2020-03-01 00:01:0" + i, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                            .category(i)
                            .content("testcontent" + i)
                            .link("www.test" + i + ".com")
                            .sourceLink("www.test" + i + "source.com")
                            .createTime(DateTimeUtil.toMongoStandardFormat(currentTime))
                            .updateTime(DateTimeUtil.toMongoStandardFormat(currentTime))
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
                .sourceTime(DateTimeUtil.toMongoStandardFormat(LocalDateTime.parse("2024-01-01 12:34:56", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                .category(1)
                .updateTime(DateTimeUtil.toMongoStandardFormat(currentTime))
                .createTime(DateTimeUtil.toMongoStandardFormat(currentTime))
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
                .createTime(DateTimeUtil.toMongoStandardFormat(currentTime))
                .updateTime(DateTimeUtil.toMongoStandardFormat(currentTime))
                .build();
        fakeNewNews = new NewNews(
                "singularTest",
                "singularTestContent",
                "singularTestSource",
                "2024-01-01 12:34:56",
                "www.singulartest.com",
                "www.singulartestsource.com",
                CategoryType.getCategoryType(1).toString(),null
        );
        fakeClassifyResult = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            fakeClassifyResult.add(Pair.of(CategoryType.getCategoryType(i), 0.1 * i));
        }
        allSourceNewsList = new ArrayList<>() {{
            add(NewsSourceCountVO.builder()
                    .source("test1")
                    .count(1)
                    .build()
            );
            add(NewsSourceCountVO.builder()
                    .source("test2")
                    .count(2)
                    .build()
            );
        }};
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

//    @Test
//    void addNewsTest() {
//        Mockito.when(newsDAOMongoMock.insert(Mockito.any())).thenReturn(fakeNewsPO.getId());
//        Mockito.when(newsDAOMongoMock.getNextNewsId()).thenReturn(fakeNewsPO.getId());
//        long id = newsServiceMongoImpl.addNews(fakeNewNews);
//        assertEquals(id, 512L);
//    }

    @Test
    void getNewsDetailTest() {
        Mockito.when(newsDAOMongoMock.getNewsById(Mockito.anyLong())).thenReturn(fakeNewsPO);
        NewsVO result = newsServiceMongoImpl.getNewsDetail(512L);
        assert newsVOsEqual(result, fakeNewsVO);
    }

//    @Test
//    void modifyNewsTest() {
//        Mockito.when(newsDAOMongoMock.updateNewsContent(Mockito.anyLong(), Mockito.anyString())).thenReturn(0);
//        Mockito.when(newsDAOMongoMock.updateNewsTitle(Mockito.anyLong(), Mockito.anyString())).thenReturn(0);
//        Mockito.when(newsDAOMongoMock.updateNewsSource(Mockito.anyLong(), Mockito.anyString())).thenReturn(0);
//        newsServiceMongoImpl.modifyNewsContent(512L, "newContent");
//        newsServiceMongoImpl.modifyNewsTitle(512L, "newTitle");
//        newsServiceMongoImpl.modifyNewsSource(512L, "newSource");
//    }

//    @Test
//    void deleteNewsTest() {
//        Mockito.when(newsDAOMongoMock.deleteById(Mockito.any())).thenReturn(0);
//        newsServiceMongoImpl.deleteNews(512L);
//    }

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
        Mockito.when(classifierMock.topNClassify(Mockito.anyString(), Mockito.anyInt())).thenReturn(fakeClassifyResult);
        List<ClassifyResultVO> result = newsServiceMongoImpl.topNClassify("test", 5);
        for (int i = 0; i < 5; i++) {
            assertTrue(CategoryType.of(i).equals(result.get(i).getCategory()));
            assertEquals(0.1 * i, result.get(i).getProbability());
        }
    }

    @Test
    void getWordCloudHitTest() {
        Mockito.when(newsDAOMongoMock.getNewsById(Mockito.anyLong())).thenReturn(fakeNewsPO);
        Mockito.when(newsDAOMongoMock.getNewsSegmentById(Mockito.anyLong())).thenReturn(NewsSegmentPO.builder()
                .id(512L)
                .content(new NewsWordPO[]{
                        NewsWordPO.builder()
                                .text("test1")
                                .count(1)
                                .build(),
                        NewsWordPO.builder()
                                .text("test2")
                                .count(2)
                                .build(),
                        NewsWordPO.builder()
                                .text("test3")
                                .count(3)
                                .build(),
                        NewsWordPO.builder()
                                .text("test4")
                                .count(4)
                                .build(),
                })
                .build()
        );
        NewsSegmentVO result = newsServiceMongoImpl.getNewsWordCloud(512L);
        assertEquals(512L, result.getId());
        assertEquals(4, result.getContent().length);
        for (int i = 0; i < result.getContent().length; i++) {
            assertEquals("test" + (i + 1), result.getContent()[i].getText());
            assertEquals(i + 1, result.getContent()[i].getCount());
        }

    }

    @Test
    void getWordCloudNullTest() {
        Mockito.when(newsDAOMongoMock.getNewsById(Mockito.anyLong())).thenReturn(fakeNewsPO);
        Mockito.when(newsDAOMongoMock.getNewsSegmentById(Mockito.anyLong())).thenReturn(null);
        Mockito.when(newsDAOMongoMock.insertSegment(Mockito.any())).thenReturn(0);
        Mockito.when(textSegmentMock.rank(Mockito.anyString())).thenReturn(new NewsWordDetail[]{
                NewsWordDetail.builder()
                        .text("test1")
                        .partOfSpeech(SpeechPart.ofTag("n"))
                        .rank(0)
                        .build(),
                NewsWordDetail.builder()
                        .text("test2")
                        .partOfSpeech(SpeechPart.ofTag("n"))
                        .rank(1)
                        .build(),
                NewsWordDetail.builder()
                        .text("test3")
                        .partOfSpeech(SpeechPart.ofTag("n"))
                        .rank(2)
                        .build(),
                NewsWordDetail.builder()
                        .text("test4")
                        .partOfSpeech(SpeechPart.ofTag("n"))
                        .rank(3)
                        .build(),
        });
        NewsSegmentVO result = newsServiceMongoImpl.getNewsWordCloud(512L);
        assertEquals(512L, result.getId());
        assertEquals(2, result.getContent().length);
        for (int i = 0; i < result.getContent().length; i++) {
            assertEquals("test" + (i + 3), result.getContent()[i].getText());
            assertEquals(1, result.getContent()[i].getCount());
        }
    }

    @Test
    public void updateWordCloudTodayTest() {
        Mockito.when(newsDAOMongoMock.getWordCloudToday()).thenReturn(null);

        newsServiceMongoImpl.updateWordCloudToday();
    }

    @Test
    public void getNewsWordCloudTodayTest0() {
        //Redis returns null
        Mockito.when(summaryServiceMock.getTopNWordCloudToday(Mockito.anyInt())).thenThrow(new RuntimeException("Test Exception"));
        Mockito.when(newsDAOMongoMock.getTopNWordCloudToday(Mockito.anyInt())).thenReturn(new ArrayList<>());
        Mockito.when(newsDAOMongoMock.getWordCloudToday()).thenReturn(null);

        assertArrayEquals(new NewsWordVO[0], newsServiceMongoImpl.getNewsWordCloudToday(10).toArray());
    }

    @Test
    public void getNewsWordCloudTodayTest1() {
        //Redis and Mongo returns null
        Mockito.when(summaryServiceMock.getTopNWordCloudToday(Mockito.anyInt())).thenThrow(new RuntimeException("Test Exception"));
        Mockito.when(newsDAOMongoMock.getTopNWordCloudToday(Mockito.anyInt())).thenReturn(null);

        assertThrows(NewsException.class, () -> newsServiceMongoImpl.getNewsWordCloudToday(10));
    }

    @Test
    public void getNewsWordCloudTodayTest2() {
        //Redis hit
        Mockito.when(summaryServiceMock.getTopNWordCloudToday(Mockito.anyInt())).thenReturn(new ArrayList<>());

        assertArrayEquals(new NewsWordVO[0], newsServiceMongoImpl.getNewsWordCloudToday(10).toArray());
    }

    @Test
    public void diffTodayAndYesterdayTest() {
        Mockito.when(summaryServiceMock.diffTodayAndYesterday()).thenReturn(0);
        assertEquals(0, newsServiceMongoImpl.diffTodayAndYesterday());
    }

    @Test
    public void countAllSourceNewsTest() {
        List<NewsSourceCountVO> mockList = new ArrayList<>() {{
            add(NewsSourceCountVO.builder()
                    .source("test1")
                    .count(1)
                    .build()
            );
            add(NewsSourceCountVO.builder()
                    .source("test2")
                    .count(2)
                    .build()
            );
        }};
        Mockito.when(summaryServiceMock.getSourceRank()).thenReturn(mockList);
        assertArrayEquals(allSourceNewsList.toArray(), newsServiceMongoImpl.countAllSourceNews().toArray());
    }

    @Test
    public void getLastModifiedTest() {
        String mockTime = "2022-03-01 00:01:01";
        Mockito.when(summaryServiceMock.getLastModified()).thenReturn(mockTime);
        assertEquals("2022-03-01 00:01:01", newsServiceMongoImpl.getLastModified());
    }

    @Test
    public void countTodayNewsTest() {
        Integer mockCount = 10;
        Mockito.when(summaryServiceMock.countDateNews()).thenReturn(mockCount);
        assertEquals(10, newsServiceMongoImpl.countTodayNews());
    }

    @Test
    public void countCategoryOfTodayTest() {
        Integer mockCount = 10;
        Mockito.when(summaryServiceMock.countCategoryNews(Mockito.anyInt())).thenReturn(mockCount);
        assertEquals(10, newsServiceMongoImpl.countCategoryOfToday(1));
    }

    @Test
    public void countAllCategoryOfTodayNewsTest() {
        List<NewsCategoryCountVO> resultList = new ArrayList<>();
        for (int i = 0; i < CategoryType.values().length; i++) {
            if (CategoryType.of(i) != CategoryType.OTHER) {
                resultList.add(
                        NewsCategoryCountVO.builder()
                                .category(CategoryType.of(i).toString())
                                .count(i)
                                .build()
                );
            }
        }
        Mockito.when(summaryServiceMock.countCategoryNews(Mockito.anyInt())).thenAnswer(
                invocation -> {
                    int i = invocation.getArgument(0);
                    return i;
                }
        );
        assertArrayEquals(resultList.toArray(), newsServiceMongoImpl.countAllCategoryOfTodayNews().toArray());
    }

    @Test
    public void countPeriodNewsTest() {
        String startTime = "2024-04-01";
        String endTime = "2024-04-01";

        String mockDate = "2024-04-01";
        List<Integer> mockList = new ArrayList<>() {{
            add(1);
            add(2);
            add(3);
        }};
        Mockito.when(summaryServiceMock.countAllCategoryOfDateNews(Mockito.any())).thenReturn(mockList);
        List<NewsDateCountVO> resultList = new ArrayList<>();
        List<NewsCategoryCountVO> singleList = new ArrayList<>();
        for (int i = 0; i < mockList.size(); i++) {
            if (CategoryType.of(i) != CategoryType.OTHER) {
                singleList.add(NewsCategoryCountVO.builder()
                        .category(CategoryType.of(i).toString())
                        .count(mockList.get(i))
                        .build()
                );
            }
        }
        resultList.add(NewsDateCountVO.builder()
                .date(LocalDate.parse(startTime).toString())
                .newsCategoryCounts(singleList)
                .build()
        );
        List<NewsDateCountVO> actualList = newsServiceMongoImpl.countPeriodNews(startTime, endTime);
        assertEquals(resultList.size(), actualList.size());
        for (int i = 0; i < actualList.size(); i++) {
            assertEquals(resultList.get(i).getDate(), actualList.get(i).getDate());
            assertEquals(
                    resultList.get(i).getNewsCategoryCounts().size(),
                    actualList.get(i).getNewsCategoryCounts().size()
            );
            for (int j = 0; j < actualList.get(i).getNewsCategoryCounts().size(); j++) {
                assertEquals(
                        resultList.get(i).getNewsCategoryCounts().get(j).getCategory(),
                        actualList.get(i).getNewsCategoryCounts().get(j).getCategory()
                );
            }
        }

    }

    @Test
    public void countWeekNewsTest() {
        Integer mockCount = 10;
        Mockito.when(summaryServiceMock.countWeekNews()).thenReturn(mockCount);
        assertEquals(10, newsServiceMongoImpl.countWeekNews());
    }

    @Test
    public void updateMongoWordCloudTodayTest() {
        List<Long> newsIdMock = new ArrayList<>() {{
            add(1L);
            add(2L);
            add(3L);
        }};
        Mockito.when(newsDAOMongoMock.getTodayNewsList()).thenReturn(newsIdMock);
        Mockito.when(newsDAOMongoMock.getNewsById(Mockito.anyLong())).thenAnswer(
                invocation -> {
                    Long id = invocation.getArgument(0);
                    if (id == 3L) return null;
                    return NewsPO.builder()
                            .id(id)
                            .title(null)
                            .content(null)
                            .category(3)
                            .build();
                }
        );
        newsServiceMongoImpl.updateWordCloudToday();
    }
    @Test
    public void saveWordCloudTest(){
        Mockito.when(newsDAOMongoMock.insertSegment(Mockito.any())).thenReturn(0);
        Assertions.assertDoesNotThrow(()->newsServiceMongoImpl.saveWordCloud(0L,List.of("1","2")));
    }

    @Test
    public void mongoExceptionTest() {

        Mockito.when(newsDAOMongoMock.getNewsById(Mockito.anyLong())).thenReturn(null);
        Mockito.when(newsDAOMongoMock.updateNewsTitle(Mockito.anyLong(), Mockito.anyString())).thenReturn(-1);
        Mockito.when(newsDAOMongoMock.updateNewsContent(Mockito.anyLong(), Mockito.anyString())).thenReturn(-1);
        Mockito.when(newsDAOMongoMock.updateNewsSource(Mockito.anyLong(), Mockito.anyString())).thenReturn(-1);
        Mockito.when(newsDAOMongoMock.deleteById(Mockito.anyLong())).thenReturn(-1);
        Mockito.when(newsDAOMongoMock.getFilteredNewsCount(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(-1L);
        Mockito.when(newsDAOMongoMock.getSearchedFilteredNewsCount(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(-1L);

        assertThrows(NewsException.class, () -> newsServiceMongoImpl.getNewsDetail(512L));
        assertThrows(NewsException.class, () -> newsServiceMongoImpl.modifyNewsTitle(512L, "newTitle"));
        assertThrows(NewsException.class, () -> newsServiceMongoImpl.modifyNewsContent(512L, "newContent"));
        assertThrows(NewsException.class, () -> newsServiceMongoImpl.modifyNewsSource(512L, "newSource"));
        assertThrows(NewsException.class, () -> newsServiceMongoImpl.deleteNews(512L));
        assertThrows(NewsException.class, () -> newsServiceMongoImpl.filterNewsPaged(1, 5, null, null, null, null));
        assertThrows(NewsException.class, () -> newsServiceMongoImpl.searchNewsByTitleFiltered(1, 5, "test", null, null, null, null));
        assertThrows(NewsException.class, () -> newsServiceMongoImpl.getNewsWordCloud(512L));
    }
}
