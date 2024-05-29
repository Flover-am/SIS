package com.seciii.prism030.core.pojo.vo.graph;

import com.seciii.prism030.core.pojo.vo.news.NewsItemVO;
import lombok.Builder;

import java.util.List;

/**
 * 实体时间轴VO
 *
 * @author wang mingsong
 * @date 2024.05.13
 */
@Builder
public class TimeAxisVO {
    /**
     * 实体名称
     */
    private String entity;
    /**
     * 相关新闻VO列表,按sourceTime排序
     */
    private List<NewsItemVO> newsList;
}
