package com.seciii.prism030.core.mapper.news;

import com.seciii.prism030.core.pojo.po.news.NewsPO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 新闻Mapper类接口
 *
 * @author wang mingsong
 * @date 2024.03.23
 */
public interface NewsMapper {
    /**
     * 获取筛选后的新闻条目数
     *
     * @param category     新闻类别
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param originSource 新闻来源
     * @return 新闻条目数
     */
    Long getFilteredNewsCount(List<Integer> category,
                              LocalDateTime startTime,
                              LocalDateTime endTime,
                              String originSource);

    /**
     * 获取筛选后的新闻列表
     *
     * @param pageSize     页码下标
     * @param pageOffset   页偏移大小
     * @param category     新闻类别
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param originSource 新闻来源
     * @return 新闻PO列表
     */
    List<NewsPO> getFilteredNewsByPage(int pageSize,
                                       int pageOffset,
                                       List<Integer> category,
                                       LocalDateTime startTime,
                                       LocalDateTime endTime,
                                       String originSource);

    /**
     * 按标题模糊搜索并筛选后的新闻条目
     *
     * @param pageSize     页码下标
     * @param pageOffset   页偏移大小
     * @param title        新闻标题
     * @param category     新闻类别
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param originSource 新闻来源
     * @return 新闻PO列表
     */
    List<NewsPO> searchFilteredNewsByPage(int pageSize,
                                          int pageOffset,
                                          String title,
                                          List<Integer> category,
                                          LocalDateTime startTime,
                                          LocalDateTime endTime,
                                          String originSource);

    /**
     * 按标题模糊搜索并筛选后的新闻条目数
     *
     * @param title        新闻标题
     * @param category     新闻类别
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param originSource 新闻来源
     * @return 新闻条目数
     */
    Long getSearchedFilteredNewsCount(String title,
                                      List<Integer> category,
                                      LocalDateTime startTime,
                                      LocalDateTime endTime,
                                      String originSource);
}
