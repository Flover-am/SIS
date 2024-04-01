package com.seciii.prism030.core.pojo.po.graph.node;

import com.seciii.prism030.core.pojo.po.graph.relationship.NewsEntityRelationship;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.util.List;

/**
 * 新闻节点类
 *
 * @author xueruichen
 * @date 2024.04.01
 */
@Node("news")
@Builder
@Data
public class NewsNode {
    @Id
    private Long id;

    @Property("title")
    private String title;

    @Relationship(type = "CONTAINS", direction = Relationship.Direction.OUTGOING)
    private List<NewsEntityRelationship> entities;
}
