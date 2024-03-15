package com.seciii.prism030.core.pojo.vo.news;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 新闻条目VO类
 *
 * @author wangmingsong
 * @date 2024.03.04
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
    private String originSource;
    /**
     * 新闻时间
     */
    @NotNull
    private String sourceTime;
    /**
     * 新闻分类
     */
    private String category;
    /**
     * 新闻更新时间
     */
    @NotNull
    private String updateTime;
    /**
     * 新闻链接
     */
    private String link;
}
