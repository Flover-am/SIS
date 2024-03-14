package com.seciii.prism030.core.pojo.po.news;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 新闻PO类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Data
@Builder
@TableName(schema="prism030", value="t_news")
public class NewsPO {
    /**
     * 新闻id
     */
    @TableId(value="id",type=IdType.AUTO)
    private Long id;
    /**
     * 新闻标题
     */
    @TableField("title")
    private String title;
    /**
     * 新闻内容
     */
    @TableField("content")
    private String content;
    /**
     * 新闻来源
     */
    @TableField("origin_source")
    private String originSource;
    /**
     * 新闻时间
     */
    @TableField("source_time")
    private LocalDateTime sourceTime;
    /**
     * 新闻链接
     */
    @TableField("link")
    private String link;
    /**
     * 新闻源链接
     */
    @TableField("source_link")
    private String sourceLink;
    /**
     * 新闻分类(暂未使用)
     */
    @TableField("category")
    private Integer category;
    /**
     * 新闻创建时间戳
     */
    @TableField(value="create_time",fill=FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 新闻更新时间戳
     */
    @TableField(value="update_time",fill=FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
