package com.seciii.prism030.core.service.impl;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.seciii.prism030.core.service.LLMService;
import com.seciii.prism030.core.utils.DashScopeUtil;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Value;

/**
 * 大模型服务实现类
 *
 * @author xueruichen
 * @date 2024.05.27
 */
public class LLMServiceImpl implements LLMService {
    @Value("${dashscope.apiKey}")
    private String apiKey;
    @Override
    public Flowable<GenerationResult> getResult(String prompt) {
        return DashScopeUtil.chat(apiKey, prompt);
    }
}
