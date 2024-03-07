package com.seciii.prism063.core.pojo.po;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * 新闻PO类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Data
@TableName(schema="prism063",value="t_news")
public class NewsPO {
    /**
     * 新闻id
     */
    @TableId(value="id",type=IdType.ASSIGN_ID)
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
    private LocalDate sourceTime;
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
    private String category;    //TODO:加入新闻分类具体实现
    /**
     * 新闻创建时间戳
     */
    @TableField(value="create_time",fill= FieldFill.INSERT)
    private LocalDate createTime;
    /**
     * 新闻更新时间戳
     */
    @TableField(value="update_time",fill=FieldFill.INSERT_UPDATE)
    private LocalDate updateTime;

}
