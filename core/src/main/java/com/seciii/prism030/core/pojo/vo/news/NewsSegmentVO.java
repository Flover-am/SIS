package com.seciii.prism030.core.pojo.vo.news;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 新闻分词结果VO类
 */
@Getter
@Setter
@Builder
public class NewsSegmentVO {
    /**
     * 新闻id
     */
    private Long id;
    /**
     * 分词结果
     */
    private NewsWordVO[] content;
}
