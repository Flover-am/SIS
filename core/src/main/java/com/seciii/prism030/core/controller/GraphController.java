package com.seciii.prism030.core.controller;

import com.seciii.prism030.common.Result;
import com.seciii.prism030.core.service.GraphService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/graph/{newsId}")
    public Result<String> analyzeGraph(@PathVariable Long newsId) {
        graphService.analyzeNews(newsId);
        return Result.success();
    }
}
