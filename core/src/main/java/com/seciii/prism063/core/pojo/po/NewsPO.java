package com.seciii.prism063.core.pojo.po;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.sql.Date;

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
    @TableId(value="pk_news_id",type=IdType.ASSIGN_ID)
    private Long id;
    /**
     * 新闻标题
     */
    @TableField("news_title")
    private String title;
    /**
     * 新闻内容
     */
    @TableField("news_content")
    private String content;
    /**
     * 新闻来源
     */
    @TableField("news_source")
    private String source;
    /**
     * 新闻时间
     */
    @TableField("news_time")
    private Date time;
    /**
     * 新闻分类(暂未使用)
     */
    @TableField("news_category")
    private String category;    //TODO:加入新闻分类具体实现
    /**
     * 新闻创建时间戳
     */
    @TableField(value="create_time",fill= FieldFill.INSERT)
    private Date createTime;
    /**
     * 新闻更新时间戳
     */
    @TableField(value="update_time",fill=FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
