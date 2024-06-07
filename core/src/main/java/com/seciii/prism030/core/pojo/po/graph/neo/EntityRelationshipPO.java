package com.seciii.prism030.core.pojo.po.graph.neo;

import lombok.Builder;
import lombok.Data;

/**
 * @author wang mingsong
 * @date 2024.06.07
 */
@Data
@Builder
public class EntityRelationshipPO {
    /**
     * Neo4j中的id
     */
    private long sourceId;
    private String sourceName;
    /**
     * 关系名称
     */
    private String relationshipName;
    private long targetId;
    private String targetName;
}
