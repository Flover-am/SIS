package com.seciii.prism030.core.service;

import com.seciii.prism030.core.pojo.po.news.NewsWordPO;

import java.time.LocalDate;
import java.util.List;

public interface SummaryService {
    void modified();

    /**
     * 获取最后一次修改时间
     *
     * @return 最后一次修改时间
     */
    String getLastModified();

    /**
     * 添加新闻
     */
    void addNews(int category/*, int id*/);

    /**
     * 删除新闻
     *
     * @param category 新闻类别
     */
    void deleteNews(int category);

    /**
     * 今日新闻数量
     *
     * @return 新闻数量
     */
    Integer countDateNews();

    /**
     * 某一天新闻数量
     *
     * @param date 日期
     * @return 新闻数量
     */
    Integer countDateNews(LocalDate date);

    /**
     * 某一类新闻数量
     *
     * @param category 新闻类别
     * @return 新闻数量
     */
    int countCategoryNews(int category);

    /**
     * 某一天某一类新闻数量
     *
     * @param category 新闻类别
     * @param date     日期
     * @return 新闻数量
     */
    int countCategoryNews(int category, LocalDate date);

    /**
     * 一周内新闻数量
     *
     * @return 一周内新闻数量
     */
    int countWeekNews();

    /**
     * 今日相较昨日新闻数量增量
     *
     * @return 新闻数量增量
     */
    Integer diffTodayAndYesterday();

    /**
     * 获取当日词云
     *
     * @param count 词云数量
     * @return 词云列表
     */
    List<NewsWordPO> getTopNWordCloudToday(int count);

    /**
     * 更新当日词云
     *
     * @param wordCloud 词云列表
     */
    void updateWordCloudToday(List<NewsWordPO> wordCloud);

}
