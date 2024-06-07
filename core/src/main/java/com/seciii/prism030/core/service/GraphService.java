package com.seciii.prism030.core.service;

import com.seciii.prism030.core.pojo.dto.NewsEntityRelationshipDTO;
import com.seciii.prism030.core.pojo.po.graph.node.EntityNode;
import com.seciii.prism030.core.pojo.po.graph.node.NewsNode;
import com.seciii.prism030.core.pojo.vo.graph.GraphVO;
import com.seciii.prism030.core.pojo.vo.graph.KnowledgeGraphVO;
import com.seciii.prism030.core.pojo.vo.graph.TimeAxisVO;

import java.util.List;

/**
 * 知识图谱服务接口
 *
 * @author xueruichen
 * @date 2024.04.01
 */
public interface GraphService {

    /**
     * 添加新闻节点
     *
     * @param newsId 新闻id
     * @param title  新闻标题
     */
    NewsNode addNewsNode(Long newsId, String title);

    /**
     * 添加实体节点
     *
     * @param newsNodeId 对应新闻id
     * @param name       实体名
     */
    EntityNode addEntityNode(Long newsNodeId, String name);


    /**
     * 添加实体间的关系
     *
     * @param fromId       关系起点实体id
     * @param toId         关系终点实体id
     * @param relationship 关系名
     */
    void addEntityRelationship(Long fromId, Long toId, String relationship);

    /**
     * 根据新闻id查询新闻对应节点及关系
     *
     * @param id 新闻id
     * @return 新闻节点
     */
    NewsNode getNewsNode(Long id);

    /**
     * 抽取新闻实体关系并持久化
     *
     * @param newsId 新闻id
     * @return 知识图谱
     */
    GraphVO analyzeNews(Long newsId);

    /**
     * 获取知识图谱
     *
     * @param newsId 新闻id
     * @return 知识图谱
     */
    GraphVO getGraph(Long newsId);

    /**
     * 获取实体时间轴
     *
     * @param entity 实体名
     * @return 时间轴
     */
    TimeAxisVO getTimeAxis(String entity);

    /**
     * 添加新闻实体关系
     *
     * @param newsId   新闻id
     * @param entities 实体关系列表
     * @return 新闻节点
     */
    NewsNode addNewsEntities(long newsId, List<NewsEntityRelationshipDTO> entities);

    /**
     * 获取知识图谱
     *
     * @param limit            返回节点上限数
     * @param firstEntityName  第一个实体名
     * @param secondEntityName 第二个实体名
     * @param relationshipName 关系名
     * @return 知识图谱节点
     */
    KnowledgeGraphVO getKnowledgeGraph(Integer limit, String firstEntityName, String secondEntityName, String relationshipName);
}
