package com.seciii.prism030.core.controller;

import com.seciii.prism030.common.Result;
import com.seciii.prism030.core.pojo.vo.graph.NewsInfoVO;
import com.seciii.prism030.core.service.GraphService;
import com.seciii.prism030.core.utils.SparkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/v1"))
@Slf4j
public class TestController {
    @Autowired
    private GraphService graphService;
    @PostMapping("/test")
    public Result<String> test(@RequestBody NewsInfoVO newsInfo) {
        graphService.analyzeNews(newsInfo.getNewsId(), newsInfo.getTitle(), newsInfo.getContent());
        return Result.success();
    }
}
