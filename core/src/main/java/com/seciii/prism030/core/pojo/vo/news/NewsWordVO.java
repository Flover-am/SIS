package com.seciii.prism030.core.pojo.vo.news;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 新闻分词元素VO类
 *
 * @author wang mingsong
 * @date 2024.04.14
 */
@Getter
@Setter
@Builder
public class NewsWordVO {
    /**
     * 词语字符串
     */
    private String text;
    /**
     * 词语出现数量
     */
    private int count;
}