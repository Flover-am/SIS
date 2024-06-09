package com.seciii.prism030.core.pojo.vo.news;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 添加新闻VO类
 *
 * @author wangmingsong
 * @date 2024.03.04
 */
@Getter
@Setter
@NoArgsConstructor
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
    private String sourceTime;
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
