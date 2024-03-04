package com.seciii.prism063.core.pojo.vo.news;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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
    private String source;
    /**
     * 新闻时间
     */
    @NotNull
    private LocalDate time;
    /**
     * 新闻分类
     */
    private String category;    //TODO:新闻分类具体实现
    /**
     * 新闻创建时间戳
     */
    @NotNull
    private LocalDate createTime;
    /**
     * 新闻更新时间戳
     */
    @NotNull
    private LocalDate updateTime;
}
