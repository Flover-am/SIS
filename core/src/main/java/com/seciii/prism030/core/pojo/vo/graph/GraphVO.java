package com.seciii.prism030.core.pojo.vo.graph;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 图谱VO类
 *
 * @author xueruichen
 * @date 2024.04.15
 */
@Data
@Builder
public class GraphVO {
    /**
     * 新闻标题
     */
    private String title;

    /**
     * 实体列表
     */
    private List<String> entityList;

    /**
     * 新闻实体关系列表
     */
    private List<NewsEntityRelationVO> newsEntityRelationList;

    /**
     * 实体关系列表
     */
    private List<EntityRelationVO> entityRelationList;
}
