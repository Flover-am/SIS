package com.seciii.prism030.core.dao.graph;

import com.seciii.prism030.core.pojo.po.graph.node.NewsNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * 新闻节点dao
 *
 * @author xueruichen
 * @date 2024.04.01
 */
@Repository
public interface NewsNodeDAO extends Neo4jRepository<NewsNode, Long> {

}
