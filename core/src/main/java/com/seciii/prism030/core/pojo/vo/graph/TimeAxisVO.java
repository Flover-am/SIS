package com.seciii.prism030.core.pojo.vo.graph;

import com.seciii.prism030.core.pojo.vo.news.NewsVO;
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
    private String entity;
    private List<NewsVO> newsList;
}
