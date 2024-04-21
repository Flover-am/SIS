package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.pojo.vo.news.NewsSourceCountVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SummaryServiceRedisImplTest {

    @InjectMocks
    private SummaryServiceRedisImpl summaryService;

    @Mock
    private RedisTemplate<String, Object> mockRedisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private ZSetOperations<String, Object> zSetOperations;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mockRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(mockRedisTemplate.opsForZSet()).thenReturn(zSetOperations);
    }

    @Test
    public void testModified() {
        summaryService.modify();
        verify(valueOperations, times(1)).set(anyString(), anyString());
    }

    @Test
    public void testGetLastModified() {
        when(valueOperations.get(anyString())).thenReturn("2024-04-16");
        String lastModified = summaryService.getLastModified();
        assertEquals("2024-04-16", lastModified);
    }

    @Test
    public void testAddNews() {
        NewNews newNews = new NewNews();
        newNews.setCategory("1");
        newNews.setOriginSource("source");
        summaryService.addNews(newNews);
        verify(valueOperations, times(2)).increment(anyString());
        verify(zSetOperations, times(1)).incrementScore(anyString(), anyString(), anyDouble());
    }

    @Test
    public void testDeleteNews() {
        summaryService.deleteNews(1);
        verify(valueOperations, times(2)).decrement(anyString());
    }

    @Test
    public void testCountDateNews() {
        when(valueOperations.get(anyString())).thenReturn(5);
        int count = summaryService.countDateNews();
        assertEquals(5, count);
    }

    @Test
    public void testCountCategoryNews() {
        when(valueOperations.get(anyString())).thenReturn(3);
        int count = summaryService.countCategoryNews(1);
        assertEquals(3, count);
    }

    @Test
    public void testCountWeekNews() {
        when(valueOperations.get(anyString())).thenReturn(2);
        int count = summaryService.countWeekNews();
        assertEquals(14, count);
    }

    @Test
    public void testDiffTodayAndYesterday() {
        when(valueOperations.get(anyString())).thenReturn(3, 2);
        int diff = summaryService.diffTodayAndYesterday();
        assertEquals(1, diff);
    }

    @Test
    public void testGetSourceRank() {
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<>();
        tuples.add(new DefaultTypedTuple<>("source", 1.0));
        when(zSetOperations.rangeWithScores(anyString(), anyLong(), anyLong())).thenReturn(tuples);

        List<NewsSourceCountVO> sourceRank = summaryService.getSourceRank();
        assertEquals(1, sourceRank.size());
        assertEquals("source", sourceRank.get(0).getSource());
        assertEquals(7, sourceRank.get(0).getCount());
    }

    @Test
    public void testCountAllCategoryOfDateNews() {
        when(valueOperations.multiGet(anyList())).thenReturn(Arrays.asList(1, 2, 3));
        List<Integer> counts = summaryService.countAllCategoryOfDateNews(LocalDate.now());
        assertEquals(3, counts.size());
    }

    // Add more tests for other public methods...
}
