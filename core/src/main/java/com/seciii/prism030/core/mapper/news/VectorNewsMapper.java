package com.seciii.prism030.core.mapper.news;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seciii.prism030.core.pojo.po.news.VectorNewsPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xueruichen
 * @date 2024.05.28
 */
@Mapper
public interface VectorNewsMapper extends BaseMapper<VectorNewsPO> {
    VectorNewsPO getVectorNewsByVectorId(String vectorId);
}
