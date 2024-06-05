package com.seciii.prism030.core.utils;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.seciii.prism030.common.exception.LLMException;
import com.seciii.prism030.common.exception.error.ErrorType;
import io.reactivex.Flowable;

import java.util.Collections;

/**
 * DashScope工具类
 *
 * @author xueruichen
 * @date 2024.05.28
 */
public class DashScopeUtil {
    private static final String MODEL = "qwen-plus";
    private static final double TOP_P = 0.8;

    /**
     * 与大模型进行交互
     *
     * @param apiKey apiKey
     * @param prompt prompt
     * @param gen Generation 对象
     * @return 输出的流式结果
     */
    public static Flowable<GenerationResult> chat(String apiKey, String prompt, Generation gen) {
        Message message = Message.builder().role(Role.USER.getValue()).content(prompt).build();
        GenerationParam param = GenerationParam.builder()
                .model(MODEL)
                .apiKey(apiKey)
                .messages(Collections.singletonList(message))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .topP(TOP_P)
                .incrementalOutput(true)
                .build();
        Flowable<GenerationResult> result;
        try {
            result = gen.streamCall(param);
        } catch (NoApiKeyException | InputRequiredException e) {
            throw new LLMException(ErrorType.LLM_REQUEST_ERROR, "大模型请求异常");
        }
        return result;
    }
}
