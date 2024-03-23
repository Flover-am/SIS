package com.seciii.prism030.core.dao.news;


import com.seciii.prism030.core.mapper.news.NewsMapper;
import com.seciii.prism030.core.pojo.po.news.NewsPO;

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
     * @param newsPO 新闻PO
     * @return 插入成功返回新闻PO，否则返回null
     */
    NewsPO insert(NewsPO newsPO);

    /**
     * 修改新闻标题
     * @param id 新闻id
     * @param title 新标题
     * @return 修改成功返回0，否则返回-1
     */
    int updateNewsTitle(Long id, String title);

    /**
     * 修改新闻内容
     * @param id 新闻id
     * @param content 新内容
     * @return 修改成功返回0，否则返回-1
     */
    int updateNewsContent(Long id, String content);

    /**
     * 修改新闻来源
     * @param id 新闻id
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
}
