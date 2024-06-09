package com.seciii.prism030.core.controller;

import com.seciii.prism030.common.Result;
import com.seciii.prism030.core.pojo.vo.graph.GraphVO;
import com.seciii.prism030.core.service.GraphService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
     * 分析生成知识图谱
     *
     * @param newsId 新闻id
     * @return 响应结果
     */
    @PostMapping("/graph/{newsId}")
    public Result<GraphVO> analyzeGraph(@PathVariable Long newsId) {
        GraphVO graph = graphService.analyzeNews(newsId);
        return Result.success(graph);
    }

    @GetMapping("/graph/{newsId}")
    public Result<GraphVO> getGraph(@PathVariable Long newsId) {
        GraphVO graph = graphService.getGraph(newsId);
        return Result.success(graph);
    }
}
