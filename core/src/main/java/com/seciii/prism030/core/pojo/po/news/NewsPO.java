package com.seciii.prism030.core.pojo.po.news;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 新闻PO类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Data
@Builder
@Document("news")
public class NewsPO implements Serializable {
    /**
     * 新闻id
     */
    @Id
    private Long id;
    /**
     * 新闻标题
     */
    @Field("title")
    private String title;
    /**
     * 新闻内容
     */
    @Field("content")
    private String content;
    /**
     * 新闻来源
     */
    @Field("origin_source")
    private String originSource;
    /**
     * 新闻时间
     */
    @Indexed
    @Field("source_time")
    private LocalDateTime sourceTime;
    /**
     * 新闻链接
     */
    @Field("link")
    private String link;
    /**
     * 新闻源链接
     */
    @Field("source_link")
    private String sourceLink;
    /**
     * 新闻分类(暂未使用)
     */
    @Field("category")
    private Integer category;
    /**
     * 新闻创建时间戳
     */
    @Field(value = "create_time")
    private LocalDateTime createTime;
    /**
     * 新闻更新时间戳
     */
    @Field(value = "update_time")
    private LocalDateTime updateTime;

    @Override
    public String toString(){
        return "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", originSource='" + originSource + '\'' +
                ", sourceTime=" + sourceTime +
                ", link='" + link + '\'' +
                ", sourceLink='" + sourceLink + '\'' +
                ", category=" + category +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
