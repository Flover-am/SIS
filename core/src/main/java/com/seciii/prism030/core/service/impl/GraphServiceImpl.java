package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.common.exception.GraphException;
import com.seciii.prism030.common.exception.error.ErrorType;
import com.seciii.prism030.core.dao.graph.EntityNodeDAO;
import com.seciii.prism030.core.dao.graph.NewsNodeDAO;
import com.seciii.prism030.core.dao.news.NewsDAOMongo;
import com.seciii.prism030.core.pojo.dto.NewsEntityRelationshipDTO;
import com.seciii.prism030.core.pojo.po.graph.neo.EntityNodePO;
import com.seciii.prism030.core.pojo.po.graph.neo.NewsNodePO;
import com.seciii.prism030.core.pojo.po.graph.node.EntityNode;
import com.seciii.prism030.core.pojo.po.graph.node.NewsNode;
import com.seciii.prism030.core.pojo.po.graph.relationship.EntityRelationship;
import com.seciii.prism030.core.pojo.po.graph.relationship.NewsEntityRelationship;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.vo.graph.*;
import com.seciii.prism030.core.service.GraphService;
import com.seciii.prism030.core.utils.NewsUtil;
import com.seciii.prism030.core.utils.SparkUtil;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Path;
import org.neo4j.driver.types.Relationship;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 知识图谱实现类
 *
 * @author xueruichen
 * @date 2024.04.01
 */
@Service
@Slf4j
public class GraphServiceImpl implements GraphService {
    private final EntityNodeDAO entityNodeDAO;
    private final NewsNodeDAO newsNodeDAO;

    private NewsDAOMongo newsDAOMongo;

    private final Neo4jClient neo4jClient;


    private final SparkUtil sparkUtil;
    private final ConcurrentHashMap<Long, Lock> newsLocks = new ConcurrentHashMap<>();

    private static final String DB_NAME = "neo4j";

    private static final int MAX_NODES = 25;

    public GraphServiceImpl(EntityNodeDAO entityNodeDAO,
                            NewsNodeDAO newsNodeDAO,
                            NewsDAOMongo newsDAOMongo,
                            SparkUtil sparkUtil,
                            Neo4jClient neo4jClient
    ) {
        this.entityNodeDAO = entityNodeDAO;
        this.newsNodeDAO = newsNodeDAO;
        this.newsDAOMongo = newsDAOMongo;
        this.sparkUtil = sparkUtil;
        this.neo4jClient = neo4jClient;
    }

    @Override
    public NewsNode addNewsNode(Long newsId, String title) {
        if (newsNodeDAO.findNewsNodeByNewsId(newsId) != null) {
            throw new GraphException(ErrorType.GRAPH_NODE_EXISTS, "新闻节点已存在");
        }
        NewsNode newsNode = NewsNode.builder()
                .newsId(newsId)
                .title(title)
                .entities(Collections.emptyList())
                .build();
        return newsNodeDAO.save(newsNode);
    }

    @Override
    public EntityNode addEntityNode(Long newsNodeId, String name) {
        NewsNode newsNode = newsNodeDAO.findById(newsNodeId).orElseThrow();

        EntityNode entityNode = entityNodeDAO.findEntityNodeByName(name);
        if (entityNode != null) {
            // 实体节点已经存在
            if (entityNode.getNewsNodeIdList().contains(newsNode.getId())) {
                // 实体节点已经与新闻节点绑定
                return entityNode;
            }
            // 绑定现存实体节点与现存新闻节点
            entityNode.getNewsNodeIdList().add(newsNode.getId());
            entityNodeDAO.save(entityNode);

            NewsEntityRelationship relationship = NewsEntityRelationship.builder()
                    .entity(entityNode)
                    .build();
            newsNode.getEntities().add(relationship);
            newsNodeDAO.save(newsNode);
            return entityNode;
        } else {
            // 创建新的实体节点
            entityNode = EntityNode.builder()
                    .name(name)
                    .entities(Collections.emptyList())
                    .newsNodeIdList(new ArrayList<>() {{
                        add(newsNode.getId());
                    }})
//                    .relationshipIdList(Collections.emptyList())
                    .build();
            EntityNode node = entityNodeDAO.save(entityNode);

            // 建立新闻节点与实体节点的联系
            NewsEntityRelationship relationship = NewsEntityRelationship.builder()
                    .entity(entityNode)
                    .build();
            newsNode.getEntities().add(relationship);
            newsNodeDAO.save(newsNode);
            return node;
        }
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

    private void addRelationshipWithCycloCheck(Long fromId, Long toId, String relationship) {

        EntityNode fromEntity = entityNodeDAO.findById(fromId).orElseThrow();
        EntityNode toEntity = entityNodeDAO.findById(toId).orElseThrow();
        if (!entityNodeDAO.isConnected(toEntity.getName(), fromEntity.getName())) {
            // 检查是否已存在对应关系
            if (entityNodeDAO.existsByNameAndTarget(relationship, toEntity.getName())) {
                return;
            }

            // 未存在对应关系，创建新的对应关系
            EntityRelationship entityRelationship = EntityRelationship.builder()
                    .entity(toEntity)
                    .relationship(relationship)
                    .build();
            fromEntity.getEntities().add(entityRelationship);
            entityNodeDAO.save(fromEntity);
        } else {
            //TODO：消除环路
            return;
        }
    }

    @Override
    public NewsNode getNewsNode(Long id) {
        return newsNodeDAO.findById(id).orElseThrow();
    }

    @Override
    public GraphVO analyzeNews(Long newsId) {
        ReentrantLock lock = (ReentrantLock) newsLocks.computeIfAbsent(newsId, k -> new ReentrantLock());
        try {
            // 尝试为该id对应的新闻加锁
            if (!lock.tryLock(1, TimeUnit.SECONDS)) {
                // 无法加锁，说明已经被锁
                throw new GraphException(ErrorType.GRAPH_GENERATING, "知识图谱正在生成中");
            }
            NewsNode newsNode = getNewsNodeByNewsId(newsId);
            if (newsNode != null) {
                // neo4j中已有该节点，直接返回
                return mapGraph(newsNode);
            }
            NewsPO news = newsDAOMongo.getNewsById(newsId);
            if (news == null) {
                // 未找到新闻，抛出异常
                throw new GraphException(ErrorType.NEWS_NOT_FOUND, "未找到对应id的新闻");
            }
            String title = news.getTitle();
            String content = news.getContent();

            // 获取大模型返回结果
            String llmResult = sparkUtil.chat(content);
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
                        log.warn("大模型返回结果异常，忽略该三元组");
                    }
                }
            }

            // 将新闻节点添加进neo4j
            newsNode = addNewsNode(newsId, title);

            for (String entity : entities.keySet()) {
                EntityNode entityNode = addEntityNode(newsNode.getId(), entity);
                entities.put(entity, entityNode.getId());
            }

            for (NewsEntityRelationshipDTO relation : relations) {
                addEntityRelationship(entities.get(relation.getEntity1()), entities.get(relation.getEntity2()), relation.getRelationship());
            }
        } catch (InterruptedException e) {
            throw new GraphException(ErrorType.ILLEGAL_ARGUMENTS);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                newsLocks.remove(newsId);
            }
        }

        NewsNode newsNode = getNewsNodeByNewsId(newsId);
        if (newsNode == null) {
            throw new GraphException(ErrorType.NODE_SAVE_FAILED, "新闻分析失败");
        }
        return mapGraph(newsNode);
    }

    @Override
    public GraphVO getGraph(Long newsId) {
        NewsNode newsNode = getNewsNodeByNewsId(newsId);
        if (newsNode == null) {
            return analyzeNews(newsId);
        }
        return mapGraph(newsNode);
    }

    /**
     * 获取实体时间轴
     *
     * @param entity 实体名
     * @return 时间轴
     */
    @Override
    public TimeAxisVO getTimeAxis(String entity) {

        EntityNode entityNode = entityNodeDAO.findEntityNodeByName(entity);
        if (entityNode == null) {
            throw new GraphException(ErrorType.ENTITY_NOT_FOUND, "未找到对应实体");
        }

        List<Long> newsNodeIdList = entityNode.getNewsNodeIdList();
        List<NewsPO> newsList = new ArrayList<>();
        for (long newsNodeId : newsNodeIdList) {
            NewsNode newsNode = newsNodeDAO.findNewsNodeByNewsId(newsNodeId);
            if (newsNode == null) {
                continue;
            }
            NewsPO newsPO = newsDAOMongo.getNewsById(newsNode.getNewsId());
            if (newsPO == null) {
                continue;
            }
            newsList.add(newsPO);
        }

        newsList.sort(Comparator.comparing(NewsPO::getSourceTime));
        return TimeAxisVO.builder()
                .entity(entity)
                .newsList(NewsUtil.toNewsItemVO(newsList))
                .build();
    }

    /**
     * 添加新闻实体关系
     *
     * @param newsNodeId 新闻id
     * @param entities   实体关系列表
     * @return 新闻节点
     */
    @Override
    public NewsNode addNewsEntities(long newsNodeId, List<NewsEntityRelationshipDTO> entities) {
        NewsNode newsNode = getNewsNodeByNewsId(newsNodeId);
        if (newsNode == null) {
            log.error(String.format("News not found with id %d. ", newsNodeId));
            throw new GraphException(ErrorType.NEWS_NOT_FOUND, "未找到对应id的新闻");
        }
        for (NewsEntityRelationshipDTO er : entities) {
            EntityNode entity1Node = addEntityNode(newsNode.getId(), er.getEntity1());
            EntityNode entity2Node = addEntityNode(newsNode.getId(), er.getEntity2());
            addRelationshipWithCycloCheck(entity1Node.getId(), entity2Node.getId(), er.getRelationship());
        }
        return newsNode;
    }

    /**
     * 获取知识图谱
     *
     * @param limit            返回节点上限数
     * @param firstEntityName  第一个实体名
     * @param secondEntityName 第二个实体名
     * @param relationshipName 关系名
     * @return 知识图谱节点
     */
    @Override
    public KnowledgeGraphVO getKnowledgeGraph(
            @Nullable Integer limit,
            @Nullable String firstEntityName,
            @Nullable String secondEntityName,
            @Nullable String relationshipName) {
        int maxNodes = limit == null ? MAX_NODES : limit;
        String query = generateQuery(firstEntityName, secondEntityName, relationshipName, maxNodes);
        List<EntityRelationVO> entityRelationVOList = new ArrayList<>();
        Set<Long> entityNodeIdSet = new HashSet<>();
        Set<Long> relationIdSet = new HashSet<>();
        Map<Long, EntityNodePO> entityNodeMap = new HashMap<>();
        List<Relationship> relationships = new ArrayList<>();
        neo4jClient.query(query).in(DB_NAME).fetchAs(EntityNodePO.class).mappedBy(
                (typeSystem, record) -> {

                    // 判断查询的目标节点是否存在，并加入entityNodeMap中
                    if (!record.get("m").isNull()) {
                        long entityNodeId = record.get("m").asNode().id();
                        if (!entityNodeIdSet.contains(entityNodeId)) {
                            EntityNodePO targetPO = EntityNodePO.builder()
                                    .id(record.get("m").asNode().id())
                                    .name(record.get("m").get("name").asString())
                                    .relatedNews(record.get("m").get("newsNodeIdList").asList(Value::asLong))
                                    .build();
                            entityNodeIdSet.add(entityNodeId);
                            entityNodeMap.put(entityNodeId, targetPO);
                        }
                    }
                    // 将源节点加入entityNodeMap中
                    long entityNodeId = record.get("n").asNode().id();
                    EntityNodePO po = EntityNodePO.builder()
                            .id(entityNodeId)
                            .name(record.get("n").get("name").asString())
                            .relatedNews(record.get("n").get("newsNodeIdList").asList(Value::asLong))
                            .build();
                    if (!entityNodeIdSet.contains(entityNodeId)) {
                        entityNodeIdSet.add(entityNodeId);
                        entityNodeMap.put(entityNodeId, po);
                    }

                    // 将所有路径中所有关系加入relationships
                    List<Path> pathList = record.get("r").asList().stream().map(
                            path -> (Path) path
                    ).toList();
                    for (Path path : pathList) {
                        for (Relationship r : path.relationships()) {
                            if (!relationIdSet.contains(r.id())) {
                                relationIdSet.add(r.id());
                                relationships.add(r);
                            }
                        }
                    }
                    // 返回值舍弃
                    return null;
                }).all();//对每个查询结果进行处理，不可省略

        // 获取KnowledgeGraphVO的各项属性
        List<String> entityList = entityNodeMap.values().stream().map(EntityNodePO::getName).toList();
        List<String> newsList = new ArrayList<>();
        List<NewsEntityRelationVO> newsEntityRelationList = new ArrayList<>();
        for (Relationship r : relationships) {
            // 只有关系的两个实体都在entityNodeMap中时，才加入entityRelationVOList
            if (entityNodeIdSet.contains(r.startNodeId()) && entityNodeIdSet.contains(r.endNodeId())) {
                entityRelationVOList.add(EntityRelationVO.builder()
                        .source(entityNodeMap.get(r.startNodeId()).getName())
                        .relation(r.get("relationship").asString())
                        .target(entityNodeMap.get(r.endNodeId()).getName())
                        .build());
            }
        }
        // 遍历所有实体节点，将其关联的新闻标题加入newsList
        for (EntityNodePO po : entityNodeMap.values()) {
            List<NewsNodePO> newsNodeList = getNewsListByIdList(po.getRelatedNews());
            newsNodeList.stream().forEach(
                    newsNodePO -> {
                        if (!newsList.contains(newsNodePO.getTitle())) {
                            newsList.add(newsNodePO.getTitle());
                            newsEntityRelationList.add(NewsEntityRelationVO.builder()
                                    .title(newsNodePO.getTitle())
                                    .entity(po.getName())
                                    .build());
                        }
                    }
            );
        }
        return KnowledgeGraphVO.builder()
                .entityList(entityList)
                .newsList(newsList)
                .entityRelationList(entityRelationVOList)
                .newsEntityRelationList(newsEntityRelationList)
                .build();
    }

    /**
     * 生成查询语句
     *
     * @param firstNodeName    第一个节点名
     * @param secondNodeName   第二个节点名
     * @param relationshipName 关系名
     * @param limit            返回节点上限数
     * @return 查询语句
     */
    private String generateQuery(String firstNodeName, String secondNodeName, String relationshipName, int limit) {
        String firstNodeQuery = (firstNodeName == null||firstNodeName.isEmpty()) ? "(n:entity)" : "(n:entity {name:'" + firstNodeName + "'})";
        if ((secondNodeName == null||secondNodeName.isEmpty()) && (relationshipName == null || relationshipName.isEmpty())) {
            return String.format(
                    "MATCH %s OPTIONAL MATCH p=(n)-[r:RELATE_TO*..2]->(m:entity) " +
                            "WHERE NOT (m)-[:RELATE_TO]->(:entity) " +
//                            "AND NOT (n)-[*]->(n)" +
                            "RETURN n, COLLECT(p) AS r LIMIT %d",
                    firstNodeQuery, limit);
        } else {
            String relationQuery = (relationshipName == null||relationshipName.isEmpty()) ? "[r:RELATE_TO*..2]" : "[r:RELATE_TO {relationship:'" + relationshipName + "'}]";
            String secondNodeQuery = (secondNodeName == null||secondNodeName.isEmpty()) ? "(m:entity)" : "(m:entity {name:'" + secondNodeName + "'})";
            return String.format(
                    "MATCH p=" + firstNodeQuery +
                            "-" + relationQuery + "->" + secondNodeQuery +
//                            "WHERE NOT (m)-[:RELATE_TO]->(:entity) " +
                            "RETURN m,n, COLLECT(p) AS r LIMIT %d", limit
            );
        }
    }

    private GraphVO mapGraph(NewsNode node) {
        // 将node映射为知识图谱
        List<EntityNode> entityList = node.getEntities().stream().map(NewsEntityRelationship::getEntity).toList();
        List<EntityRelationVO> relationList = new ArrayList<>();
        for (EntityNode entity : entityList) {
            for (EntityRelationship relationship : entity.getEntities()) {
                relationList.add(EntityRelationVO.builder()
                        .source(entity.getName())
                        .relation(relationship.getRelationship())
                        .target(relationship.getEntity().getName())
                        .build());
            }
        }
        return GraphVO.builder()
                .title(node.getTitle())
                .entityList(entityList.stream().map(EntityNode::getName).toList())
                .newsEntityRelationList(node.getEntities().stream().map(relation -> NewsEntityRelationVO.builder()
                        .title(node.getTitle())
                        .entity(relation.getEntity().getName())
                        .build()).toList())
                .entityRelationList(relationList)
                .build();
    }

    /**
     * 根据Mongo的新闻id获取新闻节点
     *
     * @param newsId Mongo新闻id
     * @return 新闻节点
     */
    private NewsNode getNewsNodeByNewsId(Long newsId) {
        return newsNodeDAO.findNewsNodeByNewsId(newsId);
    }

    /**
     * 根据id列表获取新闻节点列表
     *
     * @param newsIdList id列表
     * @return 新闻节点列表
     */
    private List<NewsNodePO> getNewsListByIdList(List<Long> newsIdList) {
        String listString = listToString(newsIdList);
        String query = String.format("MATCH (n:news) WHERE id(n) IN %s RETURN n", listString);
        return neo4jClient.query(query).in(DB_NAME).fetchAs(NewsNodePO.class).mappedBy(
                (typeSystem, record) -> NewsNodePO.builder()
                        .id(record.get("n").asNode().id())
                        .title(record.get("n").get("title").asString())
                        .build()
        ).all().stream().toList();
    }

    /**
     * 将List<Long>转为字符串
     *
     * @param list List<Long>
     * @return 字符串
     */
    private String listToString(List<Long> list) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (Long l : list) {
            sb.append(l);
            sb.append(",");
        }
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }
}
