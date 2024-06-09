package com.seciii.prism030.core.dao.graph;

import com.seciii.prism030.core.pojo.po.graph.node.EntityNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * 实体节点dao
 *
 * @author xueruichen
 * @date 2024.04.01
 */
@Repository
public interface EntityNodeDAO extends Neo4jRepository<EntityNode, Long> {
}
