package com.seciii.prism030.core.controller;

import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.seciii.prism030.common.Result;
import com.seciii.prism030.core.pojo.vo.news.TimelineUnitVO;
import com.seciii.prism030.core.service.LLMService;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 大模型控制器类
 *
 * @author xueruichen
 * @date 2024.05.27
 */
@Slf4j
@RestController
@RequestMapping("/v1")
public class LLMController {
    private final LLMService llmService;

    public LLMController(LLMService llmService) {
        this.llmService = llmService;
    }

    @GetMapping(value = "/llm", produces = "text/event-stream")
    public Flowable<GenerationResult> getOutput(@RequestParam String input) {
        return llmService.getResult(input);
    }

    @GetMapping(value = "/llm/timeline")
    public Result<List<TimelineUnitVO>> getTimeline(@RequestParam String input) {
        return Result.success(llmService.getTimeline(input));
    }
}
