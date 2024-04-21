package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.core.pojo.po.news.NewsWordPO;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.pojo.vo.news.NewsSourceCountVO;
import com.seciii.prism030.core.service.SummaryService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    //----------------------------------------key------------------------------------------------//
    private String sourceKey(String date) {
        return "newsDate:" + date + ":sources";
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


    private List<String> categoriesCountKey(String date) {
        // 0-13
        List<String> res = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            res.add(categoryCountKey(date, i));
        }
        return res;

    }


    private String dayCountKey(String date) {
        return "newsDate:" + date + ":count";
    }
    //----------------------------------------一周来源 数量------------------------------------------------//
    // 计划用Zset存来源，value为来源，score为数量，每次添加来源时，score+1

    @Override
    public List<NewsSourceCountVO> getSourceRank() {
        Map<String, Integer> rank = new HashMap<>();
        LocalDate now = LocalDate.now();
        int week = 7;
        for (int i = 0; i < week; i++) {
            LocalDate datePtr = now.minusDays(i);
            String dateStr = datePtr.toString();
            // newsDate:2024-03-11:sources
            String sourceKey = sourceKey(dateStr);
            // 获取来源数量
            var sourceCount = redisTemplate.opsForZSet().rangeWithScores(sourceKey, 0, -1);
            if (sourceCount != null) {
                for (var source : sourceCount) {
                    String sourceName = (String) source.getValue();
                    double count = source.getScore();
                    rank.put(sourceName, rank.getOrDefault(sourceName, 0) + (int) count);
                }
            }
        }
        // 排序
        List<Map.Entry<String, Integer>> list = new ArrayList<>(rank.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        List<NewsSourceCountVO> res = new ArrayList<>();
        for (var entry : list) {
            res.add(NewsSourceCountVO.builder().source(entry.getKey()).count(entry.getValue()).build());
        }
        return res;
    }

//----------------------------------------修改时间------------------------------------------------//

    /**
     * 修改最后一次修改时间
     */
    public void modify() {
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
    public void addNews(NewNews newNews) {
        int category = Integer.parseInt(newNews.getCategory());
        String source = newNews.getOriginSource();
        LocalDate now = LocalDate.now();
        // 今日新闻数量+1
        // newsDate:2024-03-11:category:1:count
        String date = now.toString();
        String dateCountKey = dayCountKey(date);
        redisTemplate.opsForValue().increment(dateCountKey);
        // 今日种类新闻数量+1
        // newsDate:2024-03-11:count
        String categoryCountKey = categoryCountKey(date, category);
        redisTemplate.opsForValue().increment(categoryCountKey);
        redisTemplate.opsForZSet().incrementScore(sourceKey(date), source, 1);

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

//----------------------------------------新闻数量------------------------------------------------//

    /**
     * 今日新闻数量
     *
     * @return 新闻数量
     */
    @Override
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
     * 一周内新闻数量
     *
     * @return 一周内新闻数量
     */
    @Override
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

    /**
     * 今日和昨日新闻数量差
     *
     * @return
     */
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


//----------------------------------------新闻分类数量------------------------------------------------//

    /**
     * 今天某一类新闻数量
     *
     * @param category 新闻类别
     * @return 新闻数量
     */
    @Override
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
    @Override
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

    @Override
    public List<Integer> countAllCategoryOfDateNews(LocalDate date) {
        // 使用mutiGet，一次性获取多个key的值
        // 0-13
        List<Integer> res = new ArrayList<>();
        var multiedGet = redisTemplate.opsForValue().multiGet(categoriesCountKey(date.toString()));
        if (multiedGet != null) {
            for (Object o : multiedGet) {
                if (o != null) {
                    res.add((Integer) o);
                } else {
                    res.add(0);
                }
            }
        }
        return res;
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
