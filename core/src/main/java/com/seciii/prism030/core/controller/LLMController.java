package com.seciii.prism030.core.controller;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.seciii.prism030.common.Result;
import com.seciii.prism030.core.service.LLMService;
import io.reactivex.Flowable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 大模型控制器类
 *
 * @author xueruichen
 * @date 2024.05.27
 */
public class LLMController {
    private final LLMService llmService;

    public LLMController(LLMService llmService) {
        this.llmService = llmService;
    }

    @PostMapping("/llm")
    public Result<Flowable<GenerationResult>> getOutput(@RequestBody String input) {
        return Result.success(llmService.getResult(input));
    }
}
