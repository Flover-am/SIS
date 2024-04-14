package com.seciii.prism030.core.pojo.po.news;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 新闻分词结果PO类
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
    private Long id;
    /**
     * 分词结果
     */
    private NewsWordPO[] content;
}
