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
    /**
     * id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 对应新闻id
     */
    @Property("newsId")
    private Long newsId;

    /**
     * 标题
     */
    @Property("title")
    private String title;

    /**
     * 实体类关系
     */
    @Relationship(type = "CONTAINS", direction = Relationship.Direction.OUTGOING)
    private List<NewsEntityRelationship> entities;
}
