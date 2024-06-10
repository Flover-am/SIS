package com.seciii.prism030.core.pojo.vo.graph;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 知识图谱VO
 *
 * @author wang mingsong
 * @date 2024.06.06
 */
@Data
@Builder
public class KnowledgeGraphVO {
    /**
     * 实体名列表
     */
    List<String> entityList;
    /**
     * 实体关系列表
     */
    List<EntityRelationVO> entityRelationList;
    /**
     * 新闻名列表
     */
    List<String> newsList;
    /**
     * 新闻实体关系列表
     */
    List<NewsEntityRelationVO> newsEntityRelationList;
}
