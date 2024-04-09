package com.seciii.prism030.core.pojo.vo.graph;

import lombok.Builder;
import lombok.Data;

/**
 * 新闻信息VO类
 *
 * @author xueruichen
 * @date 2024.04.09
 */
@Data
@Builder
public class NewsInfoVO {
    private Long newsId;
    private String title;
    private String content;
}
