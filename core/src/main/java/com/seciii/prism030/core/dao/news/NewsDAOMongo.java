package com.seciii.prism030.core.dao.news;


import com.seciii.prism030.core.mapper.news.NewsMapper;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.po.news.NewsSegmentPO;
import com.seciii.prism030.core.pojo.po.news.NewsWordPO;

import java.util.List;

/**
 * 新闻Mapper类的MongoDB DAO接口
 *
 * @author wang mingsong
 * @date 2024.03.23
 */
public interface NewsDAOMongo extends NewsMapper {
    /**
     * 获取新闻详情
     *
     * @param id 新闻id
     * @return 新闻PO
     */
    NewsPO getNewsById(Long id);

    /**
     * 插入新闻
     *
     * @param newsPO 新闻PO
     * @return 插入成功返回新闻PO，否则返回null
     */
    long insert(NewsPO newsPO);

    /**
     * 删除新闻
     *
     * @param newsPO 新闻PO
     * @return 删除成功返回0，否则返回-1
     */
    int delete(NewsPO newsPO);

    /**
     * 根据id删除新闻
     *
     * @param id 新闻id
     * @return 删除成功返回0，否则返回-1
     */
    int deleteById(Long id);

    /**
     * 修改新闻标题
     *
     * @param id    新闻id
     * @param title 新标题
     * @return 修改成功返回0，否则返回-1
     */
    int updateNewsTitle(Long id, String title);

    /**
     * 修改新闻内容
     *
     * @param id      新闻id
     * @param content 新内容
     * @return 修改成功返回0，否则返回-1
     */
    int updateNewsContent(Long id, String content);

    /**
     * 修改新闻来源
     *
     * @param id     新闻id
     * @param source 新来源
     * @return 修改成功返回0，否则返回-1
     */
    int updateNewsSource(Long id, String source);


    /**
     * 批量删除新闻
     *
     * @param ids 新闻id列表
     * @return 返回成功删除的条目数
     */
    Long batchDeleteNews(List<Long> ids);


    /**
     * 获取下一个可用的新闻id
     *
     * @return 下一个可用新闻id
     */
    Long getNextNewsId();

    /**
     * 获取新闻分词词云
     *
     * @param id 新闻id
     * @return 新闻分词词云
     */
    NewsSegmentPO getNewsSegmentById(Long id);

    /**
     * 插入新闻分词词云
     *
     * @param newsSegmentPO 新闻分词词云
     * @return 插入成功返回0，否则返回-1
     */
    int insertSegment(NewsSegmentPO newsSegmentPO);

    /**
     * 获取今日前n词云
     *
     * @param count 词云数量
     * @return 词云列表
     */
    List<NewsWordPO> getTopNWordCloudToday(int count);

    /**
     * 获取今日词云
     *
     * @return 词云列表
     */
    List<NewsWordPO> getWordCloudToday();

}
