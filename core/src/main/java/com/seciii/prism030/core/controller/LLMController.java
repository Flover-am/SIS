package com.seciii.prism030.core.controller;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.seciii.prism030.common.Result;
import com.seciii.prism030.core.pojo.vo.llm.ReliabilityVO;
import com.seciii.prism030.core.pojo.vo.llm.TimelineUnitVO;
import com.seciii.prism030.core.service.LLMService;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
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

    /**
     * 与大模型进行问答，进行流式输出
     *
     * @param input 输入
     * @return 输出
     */
    @GetMapping(value = "/llm", produces = "text/event-stream")
    public Flowable<GenerationResult> getOutput(@RequestParam String input) {
        return llmService.getResult(input);
    }

    /**
     * 与大模型进行问答，进行流式输出
     *
     * @param input 输入
     * @return 输出
     */
    @GetMapping(value = "/llm/data", produces = "text/event-stream")
    public Flowable<GenerationResult> getDataOutput(@RequestParam String input) {
        return llmService.getResult(input);
    }


    /**
     * 获取时间轴
     *
     * @param input 输入
     * @return 时间轴
     */
    @GetMapping("/llm/timeline")
    public Result<List<TimelineUnitVO>> getTimeline(@RequestParam String input) {
        return Result.success(llmService.getTimeline(input));
    }

    /**
     * 获取新闻可信度
     *
     * @param newsId 新闻id
     * @return 可信度VO
     */
    @GetMapping("/llm/reliability/{newsId}")
    public Result<ReliabilityVO> getReliability(@PathVariable @NotNull Long newsId) {
        return Result.success(llmService.getReliability(newsId));
    }
}
