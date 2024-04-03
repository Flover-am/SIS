package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.core.pojo.po.graph.node.EntityNode;
import com.seciii.prism030.core.pojo.po.graph.node.NewsNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GraphServiceImplTest {
    @Autowired
    private GraphServiceImpl graphService;

    @Test
    void test() {
        NewsNode news = graphService.addNewsNode(10L, "科比坠机");
        EntityNode node1 = graphService.addEntityNode(news.getId(), "科比");
        EntityNode node2 = graphService.addEntityNode(news.getId(), "飞机");
        graphService.addEntityRelationship(node1.getId(), node2.getId(), "乘坐");
        news = graphService.getNewsNode(news.getId());
        System.out.println(news);
    }

    @Test
    void test2() {
//        graphService.addEntityRelationship(12L, 11L, "搭载");
        NewsNode node = graphService.getNewsNode(10L);
        System.out.println(node);
    }

}
