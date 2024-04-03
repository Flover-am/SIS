package com.seciii.prism030.core.pojo.po.graph.relationship;

import com.seciii.prism030.core.pojo.po.graph.node.EntityNode;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

/**
 * 新闻和实体类关系
 *
 * @author xueruichen
 * @date 2024.04.01
 */
@RelationshipProperties
@Data
@Builder
public class NewsEntityRelationship {
    /**
     * id
     */
    @RelationshipId
    @GeneratedValue
    private Long id;

    /**
     * 关系目标实体节点
     */
    @TargetNode
    private EntityNode entity;
}
