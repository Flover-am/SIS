package com.seciii.prism030.core.pojo.po.news;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 向量id和新闻id对应关系的PO
 *
 * @author xueruichen
 * @date 2024.05.28
 */
@Data
@Builder
@TableName(schema = "prism030", value = "t_vector_news")
public class VectorNewsPO {
    /**
     * 向量id
     */
    @TableField(value = "vector_id")
    private String vectorId;

    /**
     * 新闻id
     */
    @TableField(value = "news_id")
    private Long newsId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE, update = "now()")
    private LocalDateTime updateTime;
}
