package com.seciii.prism030.core.service.impl;

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
        graphService.addNewsNode(10L, "科比坠机");
        graphService.addEntityNode(11L, "科比", 10L);
        graphService.addEntityNode(12L, "飞机", 10L);
        graphService.addEntityRelationship(11L, 12L, "乘坐");
    }

    @Test
    void test2() {
//        graphService.addEntityRelationship(12L, 11L, "搭载");
        NewsNode node = graphService.getNewsNode(10L);
        System.out.println(node);
    }

}
