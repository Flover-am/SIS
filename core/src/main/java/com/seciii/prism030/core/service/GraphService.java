package com.seciii.prism030.core.service;

import com.seciii.prism030.core.pojo.po.graph.node.EntityNode;
import com.seciii.prism030.core.pojo.po.graph.node.NewsNode;

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
     * @param title 新闻标题
     */
    NewsNode addNewsNode(Long newsId, String title);

    /**
     * 添加实体节点
     *
     * @param newsNodeId 对应新闻id
     * @param name 实体名
     */
    EntityNode addEntityNode(Long newsNodeId, String name);


    /**
     * 添加实体间的关系
     *
     * @param fromId 关系起点实体id
     * @param toId 关系终点实体id
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
     * @param title 新闻标题
     * @param content 新闻内容
     */
    void analyzeNews(Long newsId, String title, String content);
}
