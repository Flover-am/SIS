package com.seciii.prism030.core.pojo.vo.graph;

import lombok.Builder;
import lombok.Data;

/**
 * 实体关系VO类
 *
 * @author xueruichen
 * @date 2024.04.15
 */
@Data
@Builder
public class EntityRelationVO {
    /**
     * 关系主体
     */
    private String source;

    /**
     * 关系名
     */
    private String relation;

    /**
     * 关系客体
     */
    private String target;
}
