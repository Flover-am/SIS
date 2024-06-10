package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.common.exception.GraphException;
import com.seciii.prism030.core.dao.graph.EntityNodeDAO;
import com.seciii.prism030.core.dao.graph.NewsNodeDAO;
import com.seciii.prism030.core.dao.news.impl.NewsDAOMongoImpl;
import com.seciii.prism030.core.pojo.dto.NewsEntityRelationshipDTO;
import com.seciii.prism030.core.pojo.po.graph.node.EntityNode;
import com.seciii.prism030.core.pojo.po.graph.node.NewsNode;
import com.seciii.prism030.core.pojo.po.graph.relationship.EntityRelationship;
import com.seciii.prism030.core.pojo.po.graph.relationship.NewsEntityRelationship;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.utils.SparkUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Optional;

/**
 * 知识图谱服务测试类
 *
 * @author xueruichen
 * @date 2024.04.15
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class GraphServiceImplTest {
    @MockBean
    private NewsDAOMongoImpl newsDAOMongo;
    @MockBean
    private EntityNodeDAO entityNodeDAO;
    @MockBean
    private NewsNodeDAO newsNodeDAO;
    @Autowired
    @InjectMocks
    private GraphServiceImpl graphService;
    @MockBean
    private SparkUtil sparkUtil;

    @BeforeEach
    void beforeEach() {
        EntityNode entity1 = EntityNode.builder()
                .id(2L)
                .name("科比")
                .entities(new ArrayList<>())
                .build();
        EntityNode entity2 = EntityNode.builder()
                .id(3L)
                .name("飞机")
                .entities(new ArrayList<>())
                .build();
        entity1.getEntities().add(EntityRelationship.builder()
                .relationship("乘坐")
                .entity(entity2)
                .build());
        NewsNode node = NewsNode.builder()
                .id(1L)
                .newsId(10L)
                .title("科比坠机")
                .entities(new ArrayList<>())
                .build();
        NewsPO news1 = NewsPO.builder()
                .id(10L)
                .content("科比乘坐飞机")
                .title("科比坠机")
                .link("www.bilibili.com")
                .category(1)
                .originSource("www.baidu.com")
                .build();
        NewsPO news2 = NewsPO.builder()
                .id(11L)
                .content("科比乘坐飞机")
                .title("科比坠机")
                .link("www.bilibili.com")
                .category(1)
                .originSource("www.baidu.com")
                .build();
        node.getEntities().add(NewsEntityRelationship.builder().id(4L).entity(entity1).build());
        node.getEntities().add(NewsEntityRelationship.builder().id(5L).entity(entity1).build());
        Mockito.when(newsNodeDAO.findNewsNodeByNewsId(10L)).thenReturn(node);
        Mockito.when(newsNodeDAO.save(Mockito.any())).thenReturn(node);
        Mockito.when(sparkUtil.chat("科比乘坐飞机")).thenReturn("(科比 | 乘坐 | 飞机)");
        Mockito.when(newsNodeDAO.findById(1L)).thenReturn(Optional.of(node));
        Mockito.when(entityNodeDAO.save(Mockito.any())).thenReturn(entity1);
        Mockito.when(entityNodeDAO.findById(2L)).thenReturn(Optional.of(entity1));
        Mockito.when(entityNodeDAO.findById(3L)).thenReturn(Optional.of(entity2));
        Mockito.when(newsDAOMongo.getNewsById(10L)).thenReturn(news1);
        Mockito.when(newsDAOMongo.getNewsById(11L)).thenReturn(news2);
        Mockito.when(newsNodeDAO.findNewsNodeByNewsId(10L)).thenReturn(node);
    }

    @Test
    void addNewsNodeTest() {
        Assertions.assertThrows(GraphException.class, () -> graphService.addNewsNode(10L, "科比坠机"));
        Assertions.assertDoesNotThrow(() -> graphService.addNewsNode(12L, "科比开飞机"));
    }

    @Test
    void addEntityNodeTest() {
        Assertions.assertThrows(GraphException.class, () -> graphService.addEntityNode(1L, "科比"));
    }

    @Test
    void getGraphTest() {
        Assertions.assertDoesNotThrow(() -> graphService.getGraph(100L));
        Assertions.assertDoesNotThrow(() -> graphService.getGraph(10L));
    }

    @Test
    void addEntityRelationshipTest() {
        Assertions.assertDoesNotThrow(() -> graphService.addEntityRelationship(1L, 2L, "乘坐"));
        Assertions.assertDoesNotThrow(() -> graphService.addEntityRelationship(2L, 3L, "乘坐"));
    }
    @Test
    void addNewsEntitiesTest(){
        EntityNode entity=EntityNode.builder()
                .id(2L)
                .name("科比")
                .newsNodeIdList(new ArrayList<>())
                .entities(new ArrayList<>())
                .build();
        Mockito.when(entityNodeDAO.findEntityNodeByName(Mockito.any())).thenReturn(entity);
        Mockito.when(entityNodeDAO.save(Mockito.any())).thenReturn(entity);
        Mockito.when(entityNodeDAO.findById(Mockito.anyLong())).thenReturn(Optional.of(entity));
        Mockito.when(entityNodeDAO.isConnected(Mockito.anyString(),Mockito.anyString())).thenReturn(false);
        Mockito.when(entityNodeDAO.existsByNameAndTarget(Mockito.anyString(),Mockito.anyString())).thenReturn(false);
        Assertions.assertThrows(GraphException.class, ()->graphService.addNewsEntities(10L,new ArrayList<>(){{add(NewsEntityRelationshipDTO.builder().build());}}));
    }
    @Test
    void getTimeAxisTest(){
        EntityNode entity=EntityNode.builder()
                .id(2L)
                .name("科比")
                .newsNodeIdList(new ArrayList<>())
                .entities(new ArrayList<>())
                .build();
        NewsNode node = NewsNode.builder()
                .id(1L)
                .newsId(10L)
                .title("科比坠机")
                .entities(new ArrayList<>())
                .build();
        NewsPO news1 = NewsPO.builder()
                .id(10L)
                .content("科比乘坐飞机")
                .title("科比坠机")
                .link("www.bilibili.com")
                .category(1)
                .originSource("www.baidu.com")
                .build();
        Mockito.when(entityNodeDAO.findEntityNodeByName(Mockito.any())).thenReturn(entity);
        Mockito.when(newsNodeDAO.findNewsNodeByNewsId(Mockito.anyLong())).thenReturn(node);
        Mockito.when(newsDAOMongo.getNewsById(10L)).thenReturn(news1);

        Assertions.assertDoesNotThrow(()->graphService.getTimeAxis("科比"));
    }
}
