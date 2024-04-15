package com.seciii.prism030.core.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Redis服务
 *
 * @author: lidongsheng
 * @date: 2024.4.2
 */
@Service
@Transactional
public class RedisService {

    private static final String lastModifiedKey = "lastModified";
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Deprecated
    private String categoryKey(String date, int category) {
        return "newsDate:" + date + ":category:" + category;
    }

    @Deprecated
    private String dayKey(String date) {
        return "newsDate:" + date;
    }

    private String categoryCountKey(String date, int category) {
        return "newsDate:" + date + ":category:" + category + ":count";
    }


    private String dayCountKey(String date) {
        return "newsDate:" + date + ":count";
    }

    /**
     * 修改最后一次修改时间
     */
    public void modified() {
        LocalDate now = LocalDate.now();
        String date = now.toString();
        redisTemplate.opsForValue().set(lastModifiedKey, date);
    }

    /**
     * 获取最后一次修改时间
     *
     * @return 最后一次修改时间
     */
    public String getLastModified() {
        return (String) redisTemplate.opsForValue().get(lastModifiedKey);
    }

    /**
     * 添加新闻
     */
    public void addNews(int category/*, int id*/) {
        LocalDate now = LocalDate.now();
        String date = now.toString();
        // newsDate:2024-03-11:category:1:count
        String categoryCountKey = categoryCountKey(date, category);
        // newsDate:2024-03-11:count
        String dateCountKey = dayCountKey(date);
        redisTemplate.opsForValue().increment(categoryCountKey);
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

    /**
     * 删除新闻
     *
     * @param category 新闻类别
     */
    public void deleteNews(int category/*, int id*/) {
        LocalDate now = LocalDate.now();
        String date = now.toString();
        // newsDate:2024-03-11:category:1:count
        String categoryCountKey = categoryCountKey(date, category);
        // newsDate:2024-03-11:count
        String dateCountKey = dayCountKey(date);

        redisTemplate.opsForValue().decrement(categoryCountKey);
        redisTemplate.opsForValue().decrement(dateCountKey);
    }

    /**
     * 今日新闻数量
     *
     * @return 新闻数量
     */
    public Integer countDateNews() {
        return countDateNews(LocalDate.now());
    }

    /**
     * 某一天新闻数量
     *
     * @param date 日期
     * @return 新闻数量
     */
    public Integer countDateNews(LocalDate date) {
        String dateStr = date.toString();
        // newsDate:2024-03-11:count
        String dateCountKey = dayCountKey(dateStr);
        Object res = redisTemplate.opsForValue().get(dateCountKey);
        if (res != null) {
            return (Integer) res > 0 ? (Integer) res : 0;
        }
        return 0;
    }

    /**
     * 某一类新闻数量
     *
     * @param category 新闻类别
     * @return 新闻数量
     */
    public int countCategoryNews(int category) {
        return countCategoryNews(category, LocalDate.now());
    }

    /**
     * 某一天某一类新闻数量
     *
     * @param category 新闻类别
     * @param date     日期
     * @return 新闻数量
     */
    public int countCategoryNews(int category, LocalDate date) {
        String dateStr = date.toString();
        // newsDate:2024-03-11:category:1:count
        String categoryCountKey = categoryCountKey(dateStr, category);
        Object res = redisTemplate.opsForValue().get(categoryCountKey);
        if (res != null) {
            return (Integer) res > 0 ? (Integer) res : 0;
        }
        return 0;
    }

    /**
     * 一周内新闻数量
     *
     * @return 一周内新闻数量
     */
    public int countWeekNews() {
        LocalDate now = LocalDate.now();
        String date = now.toString();
        int week = 7;
        int all = 0;
        for (int i = 0; i < week; i++) {
            LocalDate datePtr = now.minusDays(i);
            String dateStr = datePtr.toString();
            // newsDate:2024-03-11:count
            String dateCountKey = dayCountKey(dateStr);
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
