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
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.vo.graph.*;
import com.seciii.prism030.core.service.GraphService;
import com.seciii.prism030.core.utils.NewsUtil;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Path;
import org.neo4j.driver.types.Relationship;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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


    private static final String DB_NAME = "neo4j";

    private static final String FIRST_NODE_TAG = "n";
    private static final String RELATION_TAG = "r";
    private static final String SECOND_NODE_TAG = "m";

    private static final String PATH_TAG = "p";
    private static final String ENTITY_NODE_TAG = "entity";
    private static final String NEWS_NODE_TAG = "news";
    private static final String ENTITY_NODE_NAME = "name";

    private static final String ENTITY_NEWS_LIST = "newsNodeIdList";

    private static final String RELATION_NAME = "relationship";

    private static final String NEWS_NODE_TITLE = "title";

    private static final String NEWS_CONTAINING_ENTITY = "entities";

    private static final int MAX_NODES = 25;

    public GraphServiceImpl(EntityNodeDAO entityNodeDAO,
                            NewsNodeDAO newsNodeDAO,
                            NewsDAOMongo newsDAOMongo,
                            Neo4jClient neo4jClient
    ) {
        this.entityNodeDAO = entityNodeDAO;
        this.newsNodeDAO = newsNodeDAO;
        this.newsDAOMongo = newsDAOMongo;
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
    public EntityNodePO addEntityNode(Long newsNodeId, String name) {
        NewsNodePO newsPO = neo4jClient.query(
                String.format("MATCH (%s:%s) WHERE id(%s)=%d RETURN %s",
                        FIRST_NODE_TAG, NEWS_NODE_TAG, FIRST_NODE_TAG, newsNodeId, FIRST_NODE_TAG)
        ).in(DB_NAME).fetchAs(NewsNodePO.class).mappedBy(
                (typeSystem, record) -> {
                    if (record.get(FIRST_NODE_TAG).isNull()) {
                        return null;
                    }
                    return NewsNodePO.builder()
                            .id(record.get(FIRST_NODE_TAG).asNode().id())
                            .title(record.get(FIRST_NODE_TAG).get(NEWS_NODE_TITLE).asString())
                            .build();
                }
        ).one().orElse(null);

        if (newsPO == null) {
            throw new GraphException(ErrorType.NEWS_NOT_FOUND, "未找到对应新闻节点");
        }

        AtomicBoolean isExist = new AtomicBoolean(false);
        EntityNodePO entityNode = neo4jClient.query(
                String.format("MATCH (%s:%s {%s:'%s'}) OPTIONAL MATCH %s=(%s:%s {%s:'%s'})-->(%s) RETURN %s,%s",
                        FIRST_NODE_TAG, ENTITY_NODE_TAG, ENTITY_NODE_NAME, name,
                        RELATION_TAG, SECOND_NODE_TAG, NEWS_NODE_TAG, NEWS_NODE_TITLE, newsPO.getTitle(), FIRST_NODE_TAG,
                        FIRST_NODE_TAG, RELATION_TAG)
        ).in(DB_NAME).fetchAs(EntityNodePO.class).mappedBy(
                (typeSystem, record) -> {
                    if (record.get(FIRST_NODE_TAG).isNull()) {
                        return null;
                    }
                    if (!record.get(RELATION_TAG).isNull()) {
                        isExist.set(true);
                    }
                    return EntityNodePO.builder()
                            .id(record.get(FIRST_NODE_TAG).asNode().id())
                            .name(record.get(FIRST_NODE_TAG).get(ENTITY_NODE_NAME).asString())
                            .relatedNews(record.get(FIRST_NODE_TAG).get(ENTITY_NEWS_LIST).asList(Value::asLong))
                            .build();
                }
        ).one().orElse(null);
        // 新闻节点已经与实体节点连接
        if (isExist.get()) return entityNode;
        // 新闻节点未与实体节点连接
        if (entityNode == null) {
            //实体节点不存在
            entityNode = EntityNodePO.builder()
                    .name(name)
                    .relatedNews(new ArrayList<>() {{
                        add(newsPO.getId());
                    }})
                    .build();
            neo4jClient.query(
                    String.format("CREATE (%s:%s {%s:'%s',%s:%s})",
                            FIRST_NODE_TAG, ENTITY_NODE_TAG, ENTITY_NODE_NAME, name, ENTITY_NEWS_LIST, listToString(entityNode.getRelatedNews()))
            ).in(DB_NAME).run();
        } else {
            //实体节点存在
            List<Long> newList = new ArrayList<>(entityNode.getRelatedNews());
            newList.add(newsPO.getId());
            neo4jClient.query(
                    String.format("MATCH (%s:%s {%s:'%s'}) SET %s.%s=%s",
                            FIRST_NODE_TAG, ENTITY_NODE_TAG, ENTITY_NODE_NAME, name,
                            FIRST_NODE_TAG, ENTITY_NEWS_LIST, listToString(newList)
                    )
            ).in(DB_NAME).run();
        }
        neo4jClient.query(
                String.format("MATCH (%s:%s {%s:'%s'}),(%s:%s {%s:'%s'}) CREATE (%s)-[:RELATE_TO]->(%s)",
                        FIRST_NODE_TAG, ENTITY_NODE_TAG, ENTITY_NODE_NAME, name,
                        SECOND_NODE_TAG, NEWS_NODE_TAG, NEWS_NODE_TITLE, newsPO.getTitle(),
                        SECOND_NODE_TAG, FIRST_NODE_TAG)
        ).in(DB_NAME).run();

        return entityNode;
    }

    @Override
    public void addEntityRelationship(Long fromId, Long toId, String relationship) {
        if (fromId.equals(toId)) {
            return;
        }
        boolean existed = !neo4jClient.query(String.format(
                "MATCH(%s:%s)-[%s:RELATE_TO{%s:'%s'}]->(%s:%s) WHERE id(%s)=%d AND id(%s)=%d RETURN %s",
                FIRST_NODE_TAG, ENTITY_NODE_TAG, RELATION_TAG, RELATION_NAME, relationship,
                SECOND_NODE_TAG, ENTITY_NODE_TAG,
                FIRST_NODE_TAG, fromId,
                SECOND_NODE_TAG, toId,
                RELATION_TAG
        )).in(DB_NAME).fetchAs(String.class).mappedBy(
                (typeSystem, record) -> {
                    if (record.get(RELATION_TAG).isNull()) {
                        return null;
                    }
                    return record.get(RELATION_TAG).asRelationship().get(RELATION_NAME).asString();
                }
        ).all().isEmpty();
        if (existed) {
            return;
        }
        String query = String.format(
                "MATCH (%s:%s),(%s:%s) WHERE id(%s)=%d AND id(%s)=%d CREATE (%s)-[:RELATE_TO {%s:'%s'}]->(%s)",
                FIRST_NODE_TAG, ENTITY_NODE_TAG, SECOND_NODE_TAG, ENTITY_NODE_TAG,
                FIRST_NODE_TAG, fromId, SECOND_NODE_TAG, toId,
                FIRST_NODE_TAG, RELATION_NAME, relationship, SECOND_NODE_TAG
        );
        System.out.println(query);
        neo4jClient.query(query).in(DB_NAME).run();
    }


    @Override
    public NewsNode getNewsNode(Long id) {
        return newsNodeDAO.findById(id).orElseThrow();
    }


    @Override
    public GraphVO getGraph(Long newsId) {

        Set<Long> entityNodeIdSet = new HashSet<>();
        Set<Long> relationSet = new HashSet<>();
        Map<Long, String> entityMap = new HashMap<>();
        List<EntityRelationVO> entityRelations = new ArrayList<>();

        String query = String.format(
                "MATCH (%s:%s {newsId:%d}) " +
                        "OPTIONAL MATCH (%s)-[CONTAINS]->(%s:%s) " +
                        "OPTIONAL MATCH %s=(%s)-[:RELATE_TO*..1]->(k:%s) WHERE %s.%s<>k.%s AND NOT (k)-[:RELATE_TO]->(:%s) " +
                        "RETURN %s,COLLECT(%s) AS %s ,COLLECT(%s) AS %s",
                FIRST_NODE_TAG, NEWS_NODE_TAG, newsId,
                FIRST_NODE_TAG, SECOND_NODE_TAG, ENTITY_NODE_TAG,
                PATH_TAG, SECOND_NODE_TAG, ENTITY_NODE_TAG, SECOND_NODE_TAG, ENTITY_NODE_NAME, ENTITY_NODE_NAME, ENTITY_NODE_TAG,
                FIRST_NODE_TAG, SECOND_NODE_TAG, NEWS_CONTAINING_ENTITY, PATH_TAG, RELATION_TAG
        );

        NewsNodePO newsNodePO = neo4jClient.query(query).in(DB_NAME).fetchAs(NewsNodePO.class).mappedBy(
                (typeSystem, record) -> {
                    if (record.get(FIRST_NODE_TAG).isNull()) {
                        return null;
                    }
                    if (!record.get(NEWS_CONTAINING_ENTITY).isNull()) {
                        record.get(NEWS_CONTAINING_ENTITY).asList(Value::asNode).stream().forEach(
                                node -> {
                                    if (!entityNodeIdSet.contains(node.id())) {
                                        entityNodeIdSet.add(node.id());
                                        entityMap.put(node.id(), node.get(ENTITY_NODE_NAME).asString());
                                    }
                                }
                        );
                    }
                    if (!record.get(RELATION_TAG).isNull()) {
                        record.get(RELATION_TAG).asList().stream().map(
                                        path -> (Path) path
                                ).filter(Objects::nonNull).toList()
                                .forEach(
                                        p -> {
                                            for (Relationship r : p.relationships()) {
                                                if (entityNodeIdSet.contains(r.startNodeId())
                                                        && entityNodeIdSet.contains(r.endNodeId())) {
                                                    if (!relationSet.contains(r.id())) {
                                                        relationSet.add(r.id());
                                                        entityRelations.add(EntityRelationVO.builder()
                                                                .source(entityMap.get(r.startNodeId()))
                                                                .relation(r.get(RELATION_NAME).asString())
                                                                .target(entityMap.get(r.endNodeId()))
                                                                .build());
                                                    }
                                                }
                                            }
                                        }
                                );

                    }
                    return NewsNodePO.builder()
                            .id(record.get(FIRST_NODE_TAG).asNode().id())
                            .title(record.get(FIRST_NODE_TAG).get(NEWS_NODE_TITLE).asString())
                            .build();
                }
        ).one().orElse(null);
        if (newsNodePO == null) {
            throw new GraphException(ErrorType.NEWS_NOT_FOUND, "未找到对应id的新闻");
        }

        return GraphVO.builder()
                .title(newsNodePO.getTitle())
                .entityList(entityMap.values().stream().toList())
                .newsEntityRelationList(entityMap.values().stream().map(
                        s -> NewsEntityRelationVO.builder()
                                .entity(s)
                                .title(newsNodePO.getTitle())
                                .build()

                ).toList())
                .entityRelationList(entityRelations)
                .build();
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
            EntityNodePO entity1Node = addEntityNode(newsNode.getId(), er.getEntity1());
            EntityNodePO entity2Node = addEntityNode(newsNode.getId(), er.getEntity2());
            addEntityRelationship(entity1Node.getId(), entity2Node.getId(), er.getRelationship());
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
        String query = generateQueryFromNodeRelationNames(firstEntityName, secondEntityName, relationshipName, maxNodes);
        List<EntityRelationVO> entityRelationVOList = new ArrayList<>();
        Set<Long> entityNodeIdSet = new HashSet<>();
        Set<Long> relationIdSet = new HashSet<>();
        Map<Long, EntityNodePO> entityNodeMap = new HashMap<>();
        List<Relationship> relationships = new ArrayList<>();
        neo4jClient.query(query).in(DB_NAME).fetchAs(EntityNodePO.class).mappedBy(
                (typeSystem, record) -> {

                    // 判断查询的目标节点是否存在，并加入entityNodeMap中
                    if (!record.get(SECOND_NODE_TAG).isNull()) {
                        long entityNodeId = record.get(SECOND_NODE_TAG).asNode().id();
                        if (!entityNodeIdSet.contains(entityNodeId)) {
                            EntityNodePO targetPO = EntityNodePO.builder()
                                    .id(record.get(SECOND_NODE_TAG).asNode().id())
                                    .name(record.get(SECOND_NODE_TAG).get(ENTITY_NODE_NAME).asString())
                                    .relatedNews(record.get(SECOND_NODE_TAG).get(ENTITY_NEWS_LIST).asList(Value::asLong))
                                    .build();
                            entityNodeIdSet.add(entityNodeId);
                            entityNodeMap.put(entityNodeId, targetPO);
                        }
                    }
                    // 将源节点加入entityNodeMap中
                    long entityNodeId = record.get(FIRST_NODE_TAG).asNode().id();
                    EntityNodePO po = EntityNodePO.builder()
                            .id(entityNodeId)
                            .name(record.get(FIRST_NODE_TAG).get(ENTITY_NODE_NAME).asString())
                            .relatedNews(record.get(FIRST_NODE_TAG).get(ENTITY_NEWS_LIST).asList(Value::asLong))
                            .build();
                    if (!entityNodeIdSet.contains(entityNodeId)) {
                        entityNodeIdSet.add(entityNodeId);
                        entityNodeMap.put(entityNodeId, po);
                    }

                    // 将所有路径中所有关系加入relationships
                    List<Path> pathList = record.get(RELATION_TAG).asList().stream().map(
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
                        .relation(r.get(RELATION_NAME).asString())
                        .target(entityNodeMap.get(r.endNodeId()).getName())
                        .build());
            }
        }
        // 遍历所有实体节点，将其关联的新闻标题加入newsList
        for (EntityNodePO po : entityNodeMap.values()) {
            List<String> newsNameList = getNewsListByIdList(po.getRelatedNews(), getLimit(maxNodes)).stream().map(
                    NewsNodePO::getTitle
            ).toList();
            newsNameList.stream().forEach(
                    newsName -> {
                        if(!newsList.contains(newsName)){
                            newsList.add(newsName);
                        }
                        newsEntityRelationList.add(NewsEntityRelationVO.builder()
                                .title(newsName)
                                .entity(po.getName())
                                .build());
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
     * 根据节点最大值获取新闻最大值
     *
     * @param limit 节点最大值
     * @return 新闻最大值
     */
    private int getLimit(int limit) {
        if (limit < 10) return 10;
        else if (limit < 20) return 5;
        else if (limit < 25) return 4;
        else if (limit < 50) return 2;
        return 1;
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
    private String generateQueryFromNodeRelationNames(String firstNodeName, String secondNodeName, String relationshipName, int limit) {
        int maxDepth = (firstNodeName == null || firstNodeName.isEmpty()) ? 1 : 2;
        String firstNodeQuery = (firstNodeName == null || firstNodeName.isEmpty())
                ? String.format("(%s:%s)", FIRST_NODE_TAG, ENTITY_NODE_TAG)
                : String.format("(%s:%s {%s:'%s'})", FIRST_NODE_TAG, ENTITY_NODE_TAG, ENTITY_NODE_NAME, firstNodeName);
        if ((secondNodeName == null || secondNodeName.isEmpty()) && (relationshipName == null || relationshipName.isEmpty())) {
            String pathCondition = (firstNodeName == null || firstNodeName.isEmpty())
                    ? String.format(
                    "WHERE NOT (%s)-[:RELATE_TO]->(:%s) ", SECOND_NODE_TAG, ENTITY_NODE_TAG)
                    : "";
            return String.format(
                    "MATCH %s OPTIONAL MATCH %s=(%s)-[%s:RELATE_TO*..%d]->(%s:%s) %s " +
//                            "AND NOT (n)-[*]->(n)" +
                            "RETURN %s,%s,COLLECT(%s) AS %s LIMIT %d",
                    firstNodeQuery, PATH_TAG, FIRST_NODE_TAG, RELATION_TAG, maxDepth, SECOND_NODE_TAG, ENTITY_NODE_TAG, pathCondition,
                    FIRST_NODE_TAG, SECOND_NODE_TAG, PATH_TAG, RELATION_TAG,
                    limit);
        } else {
            String relationQuery = (relationshipName == null || relationshipName.isEmpty())
                    ? String.format("[%s:RELATE_TO*..2]", RELATION_TAG)
                    : String.format("[%s:RELATE_TO {%s:'%s'}]", RELATION_TAG, RELATION_NAME, relationshipName);
            String secondNodeQuery = (secondNodeName == null || secondNodeName.isEmpty())
                    ? String.format("(%s:%s)", SECOND_NODE_TAG, ENTITY_NODE_TAG)
                    : String.format("(%s:%s {%s:'%s'})", SECOND_NODE_TAG, ENTITY_NODE_TAG, ENTITY_NODE_NAME, secondNodeName);

            return String.format(
                    "MATCH p=%s-%s->%s" +
//                            "WHERE NOT (m)-[:RELATE_TO]->(:entity) " +
                            "RETURN %s,%s, COLLECT(%s) AS %s LIMIT %d",
                    firstNodeQuery, relationQuery, secondNodeQuery, SECOND_NODE_TAG, FIRST_NODE_TAG, PATH_TAG, RELATION_TAG,
                    limit
            );
        }
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
    private List<NewsNodePO> getNewsListByIdList(List<Long> newsIdList, int limit) {
        String listString = listToString(newsIdList);
        String query = String.format("MATCH (%s:%s) WHERE id(%s) IN %s RETURN %s LIMIT %d",
                FIRST_NODE_TAG, NEWS_NODE_TAG, FIRST_NODE_TAG, listString, FIRST_NODE_TAG, limit);
        return neo4jClient.query(query).in(DB_NAME).fetchAs(NewsNodePO.class).mappedBy(
                (typeSystem, record) -> NewsNodePO.builder()
                        .id(record.get(FIRST_NODE_TAG).asNode().id())
                        .title(record.get(FIRST_NODE_TAG).get(NEWS_NODE_TITLE).asString())
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
