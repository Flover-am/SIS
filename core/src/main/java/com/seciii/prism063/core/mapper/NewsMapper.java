package com.seciii.prism063.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seciii.prism063.core.pojo.po.NewsPO;
import org.apache.ibatis.annotations.Mapper;


/**
 * 新闻Mapper类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Mapper
public interface NewsMapper extends BaseMapper<NewsPO> {

}
