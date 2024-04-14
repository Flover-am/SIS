package com.seciii.prism030.core.pojo.po.news;

import lombok.Builder;
import lombok.Data;

/**
 * 新闻分词元素PO类
 *
 * @author wang mingsong
 * @date 2024.04.14
 */
@Data
@Builder
public class NewsWordPO {
    /**
     * 分词
     */
    private String text;
    /**
     * 出现数量
     */
    private int count;
}
