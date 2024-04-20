package com.seciii.prism030.core.service;

import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.pojo.vo.news.NewsSourceCountVO;

import java.time.LocalDate;
import java.util.List;

public interface SummaryService {
    List<NewsSourceCountVO> getSourceRank();

    public void modify();

    /**
     * 获取最后一次修改时间
     *
     * @return 最后一次修改时间
     */
    public String getLastModified();

    /**
     * 添加新闻
     */
    public void addNews(NewNews newNews);

    /**
     * 删除新闻
     *
     * @param category 新闻类别
     */
    public void deleteNews(int category);

    /**
     * 今日新闻数量
     *
     * @return 新闻数量
     */
    public Integer countDateNews();

    /**
     * 某一天新闻数量
     *
     * @param date 日期
     * @return 新闻数量
     */
    public Integer countDateNews(LocalDate date);

    /**
     * 某一类新闻数量
     *
     * @param category 新闻类别
     * @return 新闻数量
     */
    public int countCategoryNews(int category);

    /**
     * 某一天某一类新闻数量
     *
     * @param category 新闻类别
     * @param date     日期
     * @return 新闻数量
     */
    public int countCategoryNews(int category, LocalDate date);

    List<Integer> countAllCategoryOfDateNews(LocalDate date);

    /**
     * 一周内新闻数量
     *
     * @return 一周内新闻数量
     */
    public int countWeekNews();

    Integer diffTodayAndYesterday();
}
