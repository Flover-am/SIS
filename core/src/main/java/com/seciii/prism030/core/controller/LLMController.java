package com.seciii.prism030.core.controller;

import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.seciii.prism030.common.Result;
import com.seciii.prism030.core.service.LLMService;
import io.reactivex.Flowable;
import org.springframework.web.bind.annotation.PostMapping;
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
@RestController
@RequestMapping("/v1")
public class LLMController {
    private final LLMService llmService;

    public LLMController(LLMService llmService) {
        this.llmService = llmService;
    }

    @PostMapping("/llm")
    public Flowable<String> getOutput(@RequestParam String input) {
        return llmService.getResult(input).map(generationResult -> {
            List<GenerationOutput.Choice> choices = generationResult.getOutput().getChoices();
            return String.join("", choices.stream().map(choice -> choice.getMessage().getContent()).toArray(String[]::new));
        });
    }
}
