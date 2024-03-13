package com.seciii.prism063.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seciii.prism063.core.pojo.po.NewsPO;
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
    Integer getNewsCount(@Param("category") List<Integer> category,
                         @Param("startTime") LocalDateTime startTime,
                         @Param("endTime") LocalDateTime endTime);

    List<NewsPO> getPage(@Param("pageNo") int pageNo,
                         @Param("pageSize") int pageSize,
                         @Param("category") List<Integer> category,
                         @Param("startTime") LocalDateTime startTime,
                         @Param("endTime") LocalDateTime endTime);

    List<NewsPO> searchNewsByTitle(@Param("pageNo") int pageNo,
                         @Param("pageSize") int pageSize,
                         @Param("title") String title,
                         @Param("category") List<Integer> category,
                         @Param("startTime") LocalDateTime startTime,
                         @Param("endTime") LocalDateTime endTime);

    Long getSearchResultCount(@Param("title") String title,
                                   @Param("category") List<Integer> category,
                                   @Param("startTime") LocalDateTime startTime,
                                   @Param("endTime") LocalDateTime endTime);

}
