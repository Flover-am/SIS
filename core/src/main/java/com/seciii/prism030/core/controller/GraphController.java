package com.seciii.prism030.core.controller;

import com.seciii.prism030.common.Result;
import com.seciii.prism030.core.pojo.vo.graph.GraphVO;
import com.seciii.prism030.core.pojo.vo.graph.KnowledgeGraphVO;
import com.seciii.prism030.core.pojo.vo.graph.TimeAxisVO;
import com.seciii.prism030.core.service.GraphService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;

/**
 * 图数据库控制器类
 *
 * @author xueruichen
 * @date 2024.04.09
 */
@RestController
@RequestMapping(("/v1"))
@Slf4j
public class GraphController {
    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }


    /**
     * 获取知识图谱
     *
     * @param newsId 新闻id
     * @return 响应结果
     */
    @GetMapping("/graph/{newsId}")
    public Result<GraphVO> getGraph(@PathVariable Long newsId) {
        GraphVO graph = graphService.getGraph(newsId);
        return Result.success(graph);
    }

    /**
     * 获取时间轴
     *
     * @param entity 实体名
     * @return 响应结果
     */
    @GetMapping("/timeAxis")
    public Result<TimeAxisVO> getTimeAxis(@RequestParam String entity) {
        TimeAxisVO timeAxisVO = graphService.getTimeAxis(entity);
        return Result.success(timeAxisVO);
    }

    /**
     * 获取知识图谱
     *
     * @param limit            限制长度
     * @param firstEntityName  第一个实体名
     * @param secondEntityName 第二个实体名
     * @param relationshipName 关系名
     * @return 响应结果
     */
    @GetMapping("/knowledgeGraph")
    public Result<KnowledgeGraphVO> getKnowledgeGraph(
            @RequestParam @Nullable Integer limit,
            @RequestParam @Nullable String firstEntityName,
            @RequestParam @Nullable String secondEntityName,
            @RequestParam @Nullable String relationshipName
    ) {
        KnowledgeGraphVO knowledgeGraph = graphService.getKnowledgeGraph(limit, firstEntityName, secondEntityName, relationshipName);
        return Result.success(knowledgeGraph);
    }
}
