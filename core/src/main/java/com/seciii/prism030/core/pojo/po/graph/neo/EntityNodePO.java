package com.seciii.prism030.core.pojo.po.graph.neo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author wang mingsong
 * @date 2024.06.07
 */
@Data
@Builder
public class EntityNodePO {
    /**
     * Neo4j中的id
     */
    private long id;

    /**
     * 实体名称
     */
    private String name;

    /**
     * 相关新闻id
     */
    private List<Long> relatedNews;

}
