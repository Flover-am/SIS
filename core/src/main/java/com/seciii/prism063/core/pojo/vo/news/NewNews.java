package com.seciii.prism063.core.pojo.vo.news;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class NewNews {
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
    private LocalDateTime sourceTime;
    /**
     * 新闻链接
     */

    private String link;
    /**
     * 新闻源链接
     */
    private String sourceLink;
    /**
     * 新闻分类
     */
    private String category;
}
