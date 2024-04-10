package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.common.exception.GraphException;
import com.seciii.prism030.common.exception.error.ErrorType;
import com.seciii.prism030.core.dao.graph.EntityNodeDAO;
import com.seciii.prism030.core.dao.graph.NewsNodeDAO;
import com.seciii.prism030.core.dao.news.NewsDAOMongo;
import com.seciii.prism030.core.pojo.dto.NewsEntityRelationshipDTO;
import com.seciii.prism030.core.pojo.po.graph.node.EntityNode;
import com.seciii.prism030.core.pojo.po.graph.node.NewsNode;
import com.seciii.prism030.core.pojo.po.graph.relationship.EntityRelationship;
import com.seciii.prism030.core.pojo.po.graph.relationship.NewsEntityRelationship;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.service.GraphService;
import com.seciii.prism030.core.utils.SparkUtil;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private final NewsDAOMongo newsDAOMongo;

    public GraphServiceImpl(EntityNodeDAO entityNodeDAO, NewsNodeDAO newsNodeDAO, NewsDAOMongo newsDAOMongo) {
        this.entityNodeDAO = entityNodeDAO;
        this.newsNodeDAO = newsNodeDAO;
        this.newsDAOMongo = newsDAOMongo;
    }

    @Override
    public NewsNode addNewsNode(Long newsId, String title) {
        NewsNode newsNode = NewsNode.builder()
                .newsId(newsId)
                .title(title)
                .entities(Collections.emptyList())
                .build();
        return newsNodeDAO.save(newsNode);
    }

    @Override
    public EntityNode addEntityNode(Long newsNodeId, String name) {
        EntityNode entityNode = EntityNode.builder()
                .name(name)
                .entities(Collections.emptyList())
                .build();
        EntityNode node = entityNodeDAO.save(entityNode);
        NewsNode newsNode = newsNodeDAO.findById(newsNodeId).orElseThrow();
        NewsEntityRelationship relationship = NewsEntityRelationship.builder()
                .entity(entityNode)
                .build();
        newsNode.getEntities().add(relationship);
        newsNodeDAO.save(newsNode);
        return node;
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

    @Override
    public void analyzeNews(Long newsId) {
        NewsNode newsNode = getNewsNodeByNewsId(newsId);
        if (newsNode != null) {
            // neo4j中已有该节点，直接返回
            return;
        }

        NewsPO news = newsDAOMongo.getNewsById(newsId);
        if (news == null) {
            // 未找到新闻，抛出异常
            throw new GraphException(ErrorType.NEWS_NOT_FOUND, "未找到对应id的新闻");
        }
        String title = news.getTitle();
        String content = news.getContent();
        // 将新闻节点添加进neo4j
        newsNode = addNewsNode(newsId, title);

        // 获取大模型返回结果
        String llmResult = SparkUtil.chat(content);
        Map<String, Long> entities = new HashMap<>();
        List<NewsEntityRelationshipDTO> relations = new ArrayList<>();
        if (llmResult != null) {
            // 获取结果按照模型返回结果格式解析
            String[] split = llmResult.split("\\n");
            for (String s : split) {
                String[] tuple = s.substring(1, s.length() - 1).
                        split(" \\| ");
                if (tuple.length == 3) {
                    entities.put(tuple[0], 0L);
                    entities.put(tuple[2], 0L);
                    relations.add(NewsEntityRelationshipDTO.builder()
                                    .entity1(tuple[0])
                                    .entity2(tuple[2])
                                    .relationship(tuple[1])
                                    .build());
                } else {
                    throw new GraphException(ErrorType.LLM_RESULT_ERROR, "大模型返回结果异常");
                }
            }
        }

        for (String entity : entities.keySet()) {
            EntityNode entityNode = addEntityNode(newsNode.getId(), entity);
            entities.put(entity, entityNode.getId());
        }

        for (NewsEntityRelationshipDTO relation : relations) {
            addEntityRelationship(entities.get(relation.getEntity1()), entities.get(relation.getEntity2()), relation.getRelationship());
        }
    }

    private NewsNode getNewsNodeByNewsId(Long newsId) {
        return newsNodeDAO.findNewsNodeByNewsId(newsId);
    }
}
