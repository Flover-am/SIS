package com.seciii.prism030.core.mapper.news;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 新闻Mapper类的MybatisPlus接口
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Mapper
public interface NewsMapperMP extends BaseMapper<NewsPO>, NewsMapper {
}
