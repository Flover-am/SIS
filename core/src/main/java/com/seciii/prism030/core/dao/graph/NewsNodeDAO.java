package com.seciii.prism030.core.dao.graph;

import com.seciii.prism030.core.pojo.po.graph.node.NewsNode;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 新闻节点dao
 *
 * @author xueruichen
 * @date 2024.04.01
 */
@Repository
public interface NewsNodeDAO extends Neo4jRepository<NewsNode, Long> {

    @Query("MATCH (n:news {newsId:$newsId}) RETURN n LIMIT 1")
    NewsNode findNewsNodeByNewsId(Long newsId);

}
