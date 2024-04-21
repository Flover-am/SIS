package com.seciii.prism030.core.service;

import com.seciii.prism030.core.pojo.dto.PagedNews;
import com.seciii.prism030.core.pojo.po.news.NewsSegmentPO;
import com.seciii.prism030.core.pojo.vo.news.ClassifyResultVO;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.pojo.vo.news.NewsSegmentVO;
import com.seciii.prism030.core.pojo.vo.news.NewsVO;
import com.seciii.prism030.core.pojo.vo.news.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 新闻服务接口类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
public interface NewsService {

    /**
     * 获取所有新闻
     *
     * @return 所有新闻列表
     */
    Integer countTodayNews();

    /**
     * 获取所有新闻
     *
     * @return 所有新闻列表
     */
    String getLastModified();

    /**
     * 获取所有新闻
     *
     * @return 所有新闻列表
     */

    Integer countCategoryOfToday(int category);

    /**
     * 获取所有新闻
     *
     * @return 所有新闻列表
     */
    List<NewsCategoryCountVO> countAllCategoryOfTodayNews();

    /**
     * 获取所有新闻
     *
     * @return 所有新闻列表
     */
    List<NewsDateCountVO> countPeriodNews(String startTime, String endTime);

    /**
     * 获取所有新闻
     *
     * @return 所有新闻列表
     */
    Integer countWeekNews();

    /**
     * 获取新闻详情
     *
     * @param id 新闻id
     * @return 新闻VO
     */
    NewsVO getNewsDetail(Long id);

    /**
     * 修改新闻标题
     *
     * @param id    新闻id
     * @param title 新标题
     */
    void modifyNewsTitle(Long id, String title);

    /**
     * 修改新闻内容
     *
     * @param id      新闻id
     * @param content 新内容
     */
    void modifyNewsContent(Long id, String content);

    /**
     * 修改新闻来源
     *
     * @param id     新闻id
     * @param source 新来源
     */
    void modifyNewsSource(Long id, String source);

    /**
     * 删除新闻
     *
     * @param id 新闻id
     */
    void deleteNews(Long id);

    /**
     * 批量删除新闻
     *
     * @param idList 新闻id列表
     */
    void deleteMultipleNews(List<Long> idList);

    /**
     * 新增新闻
     *
     * @param newNews 新增的新闻对象
     * @return 数据库中新闻插入编号
     */
    long addNews(NewNews newNews);

    /**
     * 分页获取过滤后的新闻列表
     *
     * @param pageNo       页数
     * @param pageSize     页大小
     * @param category     新闻分类
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param originSource 新闻来源
     * @return 过滤后的新闻列表
     */
    PagedNews filterNewsPaged(int pageNo, int pageSize, List<String> category, LocalDateTime startTime, LocalDateTime endTime, String originSource);

    /**
     * 按标题模糊搜索新闻并过滤,分页返回
     *
     * @param pageNo       页数
     * @param pageSize     页大小
     * @param title        新闻标题
     * @param category     新闻分类
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param originSource 新闻来源
     * @return 搜索结果新闻列表
     */
    PagedNews searchNewsByTitleFiltered(int pageNo, int pageSize, String title, List<String> category, LocalDateTime startTime, LocalDateTime endTime, String originSource);

    /**
     * 获取新闻前N可能的分类结果
     *
     * @param text 新闻标题字符串
     * @param topN topN个数
     * @return topN分类结果
     */
    List<ClassifyResultVO> topNClassify(String text, int topN);

    /**
     * 获取新闻词云
     *
     * @param id 新闻id
     * @return 词云结果
     */
    NewsSegmentVO getNewsWordCloud(long id);

    /**
     * 生成并保存新闻词云
     *
     * @param id   新闻id
     * @param text 新闻内容
     * @return 词云结果
     */
    NewsSegmentPO generateAndSaveWordCloud(long id, String text);

    /**
     * 获取今日新闻词云
     *
     * @param count 词语数量
     * @return 词云结果
     */
    List<NewsWordVO> getNewsWordCloudToday(int count);

    /**
     * 更新当日词云到Redis中
     */
    void updateWordCloudToday();

    Integer diffTodayAndYesterday();

    List<NewsSourceCountVO> countAllSourceNews();
}
