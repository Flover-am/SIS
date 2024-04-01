package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.core.dao.graph.EntityNodeDAO;
import com.seciii.prism030.core.dao.graph.NewsNodeDAO;
import com.seciii.prism030.core.pojo.po.graph.node.EntityNode;
import com.seciii.prism030.core.pojo.po.graph.node.NewsNode;
import com.seciii.prism030.core.pojo.po.graph.relationship.EntityRelationship;
import com.seciii.prism030.core.pojo.po.graph.relationship.NewsEntityRelationship;
import com.seciii.prism030.core.service.GraphService;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 知识图谱实现类
 *
 * @author xueruichen
 * @date 2024.04.01
 */
@Service
public class GraphServiceImpl implements GraphService {

    private final EntityNodeDAO entityNodeDAO;

    private final NewsNodeDAO newsNodeDAO;

    public GraphServiceImpl(EntityNodeDAO entityNodeDAO, NewsNodeDAO newsNodeDAO) {
        this.entityNodeDAO = entityNodeDAO;
        this.newsNodeDAO = newsNodeDAO;
    }

    @Override
    public void addNewsNode(Long id, String title) {
        NewsNode newsNode = NewsNode.builder()
                .id(id)
                .title(title)
                .entities(Collections.emptyList())
                .build();
        newsNodeDAO.save(newsNode);
    }

    @Override
    public void addEntityNode(Long id, String name, Long newsId) {
        EntityNode entityNode = EntityNode.builder()
                .id(id)
                .name(name)
                .entities(Collections.emptyList())
                .build();
        entityNodeDAO.save(entityNode);
        NewsNode newsNode = newsNodeDAO.findById(newsId).orElseThrow();
        NewsEntityRelationship relationship = NewsEntityRelationship.builder()
                .entity(entityNode)
                .build();
        newsNode.getEntities().add(relationship);
        newsNodeDAO.save(newsNode);
    }

    @Override
    public void addEntityRelationship(Long fromId, Long toId, String relationship) {
        EntityNode fromEntity = entityNodeDAO.findById(fromId).orElseThrow();
        EntityNode toEntity = entityNodeDAO.findById(toId).orElseThrow();
        EntityRelationship entityRelationship = EntityRelationship.builder()
                .entity(toEntity)
                .relationship(relationship)
                .build();
        fromEntity.getEntities().add(entityRelationship);
        entityNodeDAO.save(fromEntity);
    }

    @Override
    public NewsNode getNewsNode(Long id) {
        return newsNodeDAO.findById(id).orElseThrow();
    }
}
