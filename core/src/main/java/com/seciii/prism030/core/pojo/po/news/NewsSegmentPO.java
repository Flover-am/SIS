package com.seciii.prism030.core.pojo.po.news;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 新闻分词结果PO类
 *
 * @author wang mingsong
 * @date 2024.04.14
 */
@Data
@Builder
@Document("news_segment")
public class NewsSegmentPO {
    /**
     * 新闻id
     */
    @Id
    private Long id;
    /**
     * 分词结果
     */
    @Field("content")
    private NewsWordPO[] content;
}
