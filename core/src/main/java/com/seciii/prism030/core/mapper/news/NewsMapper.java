package com.seciii.prism030.core.mapper.news;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 新闻Mapper类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Mapper
public interface NewsMapper extends BaseMapper<NewsPO> {
    /**
     * 获取筛选后的新闻条目数
     *
     * @param category     新闻类别
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param originSource 新闻来源
     * @return 新闻条目数
     */
    Long getFilteredNewsCount(@Param("category") List<Integer> category,
                              @Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endTime,
                              @Param("originSource") String originSource);

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
    List<NewsPO> getFilteredNewsByPage(@Param("pageSize") int pageSize,
                                       @Param("pageOffset") int pageOffset,
                                       @Param("category") List<Integer> category,
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime,
                                       @Param("originSource") String originSource);

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
    List<NewsPO> searchFilteredNewsByPage(@Param("pageSize") int pageSize,
                                          @Param("pageOffset") int pageOffset,
                                          @Param("title") String title,
                                          @Param("category") List<Integer> category,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime,
                                          @Param("originSource") String originSource);

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
    Long getSearchedFilteredNewsCount(@Param("title") String title,
                                      @Param("category") List<Integer> category,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime,
                                      @Param("originSource") String originSource);

}
