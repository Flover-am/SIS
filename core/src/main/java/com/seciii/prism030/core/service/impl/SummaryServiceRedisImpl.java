package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.core.pojo.po.news.NewsWordPO;
import com.seciii.prism030.core.service.SummaryService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Redis服务
 *
 * @author lidongsheng
 * @date 2024.4.2
 */
@Service
@Transactional
public class SummaryServiceRedisImpl implements SummaryService {

    private static final String lastModifiedKey = "lastModified";

    private static final String wordCloudKey = "wordCloudToday";
    private final RedisTemplate<String, Object> redisTemplate;

    public SummaryServiceRedisImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
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


    @Override
    public Integer diffTodayAndYesterday() {
        LocalDate now = LocalDate.now();
        String today = now.toString();
        String yesterday = now.minusDays(1).toString();
        // newsDate:2024-03-11:count
        String todayCountKey = dayCountKey(today);
        // newsDate:2024-03-10:count
        String yesterdayCountKey = dayCountKey(yesterday);
        Object resToday = redisTemplate.opsForValue().get(todayCountKey);
        Object resYesterday = redisTemplate.opsForValue().get(yesterdayCountKey);
        int todayCount = 0;
        int yesterdayCount = 0;
        if (resToday != null) {
            todayCount = (Integer) resToday > 0 ? (Integer) resToday : 0;
        }
        if (resYesterday != null) {
            yesterdayCount = (Integer) resYesterday > 0 ? (Integer) resYesterday : 0;
        }
        return todayCount - yesterdayCount;
    }

    /**
     * 获取当日词云
     *
     * @param count 词云数量
     * @return 词云列表
     */
    @Override
    public List<NewsWordPO> getTopNWordCloudToday(int count) {
        var resSet = redisTemplate.opsForZSet().reverseRangeWithScores(
                wordCloudKey, 0, count - 1
        );
        if (resSet == null) {
            return null;
        }
        return resSet.stream().map(
                x -> NewsWordPO.builder()
                        .text((String) x.getValue())
                        .count((int) Math.floor(x.getScore()))
                        .build()
        ).toList();
    }

    /**
     * 更新当日词云
     *
     * @param wordCloud 词云列表
     */
    @Override
    public void updateWordCloudToday(List<NewsWordPO> wordCloud) {
        redisTemplate.delete(wordCloudKey);
        wordCloud.forEach(
                x -> redisTemplate.opsForZSet().add(
                        wordCloudKey, x.getText(), x.getCount()
                )
        );
        redisTemplate.expire(wordCloudKey, 1, TimeUnit.HOURS);
    }
}
