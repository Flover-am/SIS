package com.seciii.prism030.core.pojo.po.graph.neo;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author wang mingsong
 * @date 2024.06.07
 */
@Data
@Builder
public class NewsEntityRelationshipPO {
    private String title;
    private String target;
}
