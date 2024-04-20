package com.seciii.prism030.core.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SummaryServiceRedisImplTest {

    @InjectMocks
    private SummaryServiceRedisImpl summaryService;

    @Mock
    private RedisTemplate<String, Object> mockRedisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mockRedisTemplate.opsForValue()).thenReturn(valueOperations);
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
        summaryService.addNews(1);
        verify(valueOperations, times(2)).increment(anyString());
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
}
