package com.seciii.prism030.core.pojo.vo.news;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 新闻VO类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Getter
@Setter
@Builder
public class NewsVO {
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
     * 新闻内容
     */
    @NotNull
    private String content;
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
     * 新闻链接
     */
    @NotNull
    private String link;
    /**
     * 新闻源链接
     */
    private String sourceLink;
    /**
     * 新闻分类
     */
    private String category;
    /**
     * 新闻创建时间戳
     */
    @NotNull
    private String createTime;
    /**
     * 新闻更新时间戳
     */
    @NotNull
    private String updateTime;
}
