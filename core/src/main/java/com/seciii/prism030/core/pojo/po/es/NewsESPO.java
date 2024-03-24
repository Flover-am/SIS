package com.seciii.prism030.core.pojo.po.es;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "news-index")
public class NewsESPO {
    /**
     * ES文档id,固定为String类型，不要自己修改
     */
    @Id
    private String id;

    /**
     * 新闻id，记录来自数据库的id
     */
    @Field(type = FieldType.Long)
    private Long newsId;

    /**
     * 新闻标题
     */
    @Field(type = FieldType.Text)
    private String title;
    /**
     * 新闻内容
     */
    @Field(type = FieldType.Text)
    private String content;
    /**
     * 新闻来源
     */
    @Field(type = FieldType.Text)
    private String originSource;
    /**
     * 新闻时间
     */
    @Field(type = FieldType.Date,format = DateFormat.custom, pattern ="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sourceTime;
    /**
     * 新闻链接
     */
    @Field(type = FieldType.Text)
    private String link;
    /**
     * 新闻源链接
     */
    @Field(type = FieldType.Text)
    private String sourceLink;
    /**
     * 新闻分类(暂未使用)
     */
    @Field(type = FieldType.Integer)
    private Integer category;
    /**
     * 新闻创建时间戳
     */
    @Field(type = FieldType.Date,format = DateFormat.custom, pattern ="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 新闻更新时间戳
     */
    @Field(type = FieldType.Date,format = DateFormat.custom, pattern ="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
