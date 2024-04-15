package com.seciii.prism030.core.pojo.dto;

import com.seciii.prism030.core.enums.SpeechPart;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 新闻分词结果类
 *
 * @author wang mingsong
 * @date 2024.04.14
 */
@Setter
@Getter
@Builder
public class NewsWordDetail {
    /**
     * 词语字符串
     */
    private String text;
    /**
     * 词语词性
     */
    private SpeechPart partOfSpeech;
    /**
     * 词语语义权重
     */
    private int rank;
}
