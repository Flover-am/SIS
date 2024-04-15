package com.seciii.prism030.core.pojo.vo.graph;

import lombok.Builder;
import lombok.Data;

/**
 * 新闻与实体关系VO类
 *
 * @author xueruichen
 * @date 2024.04.15
 */
@Data
@Builder
public class NewsEntityRelationVO {
    /**
     * 新闻标题名
     */
    private String title;

    /**
     * 实体名
     */
    private String entity;
}
