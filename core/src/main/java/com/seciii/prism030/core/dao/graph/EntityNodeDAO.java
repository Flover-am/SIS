package com.seciii.prism030.core.dao.graph;

import com.seciii.prism030.core.pojo.po.graph.node.EntityNode;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

/**
 * 实体节点dao
 *
 * @author xueruichen
 * @date 2024.04.01
 */
@Repository
public interface EntityNodeDAO extends Neo4jRepository<EntityNode, Long> {
    @Query("MATCH (n:entity {name:$name}) OPTIONAL MATCH (n)-[r:RELATE_TO*]->(related:entity) RETURN n, COLLECT(r) AS relationships, COLLECT(related) AS relatedNodes")
    EntityNode findEntityNodeByName(String name);

    @Query("MATCH p=(n:entity {name:$name1})-[*]->(m:entity {name:$name2}) RETURN COUNT(p)>0")
    boolean isConnected(String name1, String name2);

    @Query("MATCH (n:entity)-[r]->(m:entity) WHERE r.relationship=$name AND m.name=$targetName RETURN COUNT(r)>0")
    boolean existsByNameAndTarget(String name, String targetName);

    @Query("MATCH p=(n:entity {name:$name1})-[*]->(m:entity {name:$name2})-[*]->(n) RETURN COUNT(p)>0")
    boolean hasCycle(String name1,String name2);

}
