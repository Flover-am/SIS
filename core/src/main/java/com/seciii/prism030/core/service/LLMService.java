package com.seciii.prism030.core.service;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import io.reactivex.Flowable;

/**
 * @author xueruichen
 * @date 2024.05.27
 */
public interface LLMService {
    /**
     * 获取大模型输出
     *
     * @param prompt prompt
     * @return 输出的流式结果
     */
    Flowable<GenerationResult> getResult(String prompt);
}
