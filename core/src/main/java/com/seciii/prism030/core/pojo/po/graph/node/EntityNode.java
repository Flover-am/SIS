package com.seciii.prism030.core.pojo.po.graph.node;

import com.seciii.prism030.core.pojo.po.graph.relationship.EntityRelationship;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

/**
 * 新闻实体节点类
 *
 * @author xueruichen
 * @date 2024.04.01
 */
@Node("entity")
@Builder
@Data
public class EntityNode {
    @Id
    private Long id;

    @Property("name")
    private String name;

    @Relationship(type = "RELATE_TO", direction = Relationship.Direction.OUTGOING)
    private List<EntityRelationship> entities;
}
