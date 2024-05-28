package com.seciii.prism030.core.pojo.po.news;

import lombok.Builder;
import lombok.Data;

/**
 * 向量id和新闻id对应关系的PO
 *
 * @author xueruichen
 * @date 2024.05.28
 */
@Data
@Builder
public class VectorNewsPO {
    /**
     * 向量id
     */
    private Long vectorId;

    /**
     * 新闻id
     */
    private Long newsId;
}
