package com.seciii.prism063.core.pojo.vo.news;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 新闻条目VO类
 *
 * @date 2024.03.04
 * @auther wangmingsong
 */
@Getter
@Setter
@Builder
public class NewsItemVO {
    /**
     * 新闻id
     */
    @NotNull
    private Long id;
    /**
     * 新闻标题
     */
    @NotNull
    private String title;
    /**
     * 新闻来源
     */
    @NotNull
    private String source;
    /**
     * 新闻时间
     */
    @NotNull
    private String time;
    /**
     * 新闻分类
     */
    private String category; //TODO:新闻分类具体实现
}
