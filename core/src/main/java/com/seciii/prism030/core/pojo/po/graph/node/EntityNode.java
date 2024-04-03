package com.seciii.prism030.core.pojo.po.graph.node;

import com.seciii.prism030.core.pojo.po.graph.relationship.EntityRelationship;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

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
    /**
     * id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 名称
     */
    @Property("name")
    private String name;

    /**
     * 实体关系
     */
    @Relationship(type = "RELATE_TO", direction = Relationship.Direction.OUTGOING)
    private List<EntityRelationship> entities;
}
