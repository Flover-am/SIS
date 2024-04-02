package com.seciii.prism030.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.ObjectError;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * Redis服务
 *
 * @author: lidongsheng
 * @date: 2024.4.2
 */
@Service
@Transactional
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     *
     */
    public void addNews(int category/*, int id*/) {
        LocalDate now = LocalDate.now();
        String date = now.toString();
        // newsDate:2024-03-11:category:1:count
        String categoryCountKey = "newsDate:" + date + ":category:" + category + ":count";
        // newsDate:2024-03-11:count
        String dateCountKey = "newsDate:" + date + ":count";
        redisTemplate.opsForValue().increment(String.valueOf(categoryCountKey));
        redisTemplate.opsForValue().increment(dateCountKey);

//        // newsDate:2024-03-11:category:1
//        String categoryKey = "newsDate:" + date + ":category:" + category;
//        // newsDate:2024-03-11
//        String dateKey = "newsDate:" + date;
//        redisTemplate.opsForSet().add(dateKey, id);
//        redisTemplate.opsForSet().add(categoryKey, id);
//        redisTemplate.expireAt(dateKey,new Date());
//        redisTemplate.expireAt(categoryKey,new Date());

    }

    public void deleteNews(int category/*, int id*/) {
        LocalDate now = LocalDate.now();
        String date = now.toString();
        // newsDate:2024-03-11:category:1:count
        String categoryCountKey = "newsDate:" + date + ":category:" + category + ":count";
        // newsDate:2024-03-11:count
        String dateCountKey = "newsDate:" + date + ":count";

        redisTemplate.opsForValue().decrement(categoryCountKey);
        redisTemplate.opsForValue().decrement(dateCountKey);
    }

    public Integer countDateNews() {
        LocalDate now = LocalDate.now();
        String date = now.toString();
        // newsDate:2024-03-11:count
        String dateCountKey = "newsDate:" + date + ":count";
        Object res = redisTemplate.opsForValue().get(dateCountKey);
        if (res != null) {
            return (Integer) res > 0 ? (Integer) res : 0;
        }
        return 0;
    }

    public int countCategoryNews(int category) {
        LocalDate now = LocalDate.now();
        String date = now.toString();
        // newsDate:2024-03-11:category:1:count
        String categoryCountKey = "newsDate:" + date + ":category:" + category + ":count";
        Object res = redisTemplate.opsForValue().get(categoryCountKey);
        if (res != null) {
            return (Integer) res > 0 ? (Integer) res : 0;
        }
        return 0;
    }

    public int countWeekNews() {
        LocalDate now = LocalDate.now();
        String date = now.toString();
        int week = 7;
        int all = 0;
        for (int i = 0; i < week; i++) {
            LocalDate datePtr = now.minusDays(i);
            String dateStr = datePtr.toString();
            // newsDate:2024-03-11:count
            String dateCountKey = "newsDate:" + dateStr + ":count";
            Object res = redisTemplate.opsForValue().get(dateCountKey);
            int count = 0;
            if (res != null) {
                count = (Integer) res > 0 ? (Integer) res : 0;
            }
            all += count;
        }
        return all;
    }
}
