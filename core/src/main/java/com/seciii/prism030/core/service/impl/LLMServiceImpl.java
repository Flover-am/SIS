package com.seciii.prism030.core.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.aliyun.dashvector.DashVectorClient;
import com.aliyun.dashvector.DashVectorCollection;
import com.seciii.prism030.common.exception.LLMException;
import com.seciii.prism030.common.exception.error.ErrorType;
import com.seciii.prism030.core.config.poolconfig.pool.DashVectorClientPool;
import com.seciii.prism030.core.config.poolconfig.pool.GenerationPool;
import com.seciii.prism030.core.service.LLMService;
import com.seciii.prism030.core.utils.DashScopeUtil;
import com.seciii.prism030.core.utils.DashVectorUtil;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 大模型服务实现类
 *
 * @author xueruichen
 * @date 2024.05.27
 */
@Service
public class LLMServiceImpl implements LLMService {
    @Value("${dashscope.apiKey}")
    private String apiKey;
    private final GenerationPool generationPool;
    private final DashVectorClientPool dashVectorClientPool;
    private static final String PROMPT = "请根据以上体育新闻内容，回答我的问题：";

    public LLMServiceImpl(GenerationPool generationPool, DashVectorClientPool dashVectorClientPool) {
        this.generationPool = generationPool;
        this.dashVectorClientPool = dashVectorClientPool;
    }

    @Override
    public Flowable<GenerationResult> getResult(String input) {
        Generation gen = null;
        Flowable<GenerationResult> result;
        String prompt = buildPrompt(input);
        try {
            // 从池中取对象
            gen = generationPool.borrowObject();
            result = DashScopeUtil.chat(apiKey, prompt, gen);
        } catch (Exception e) {
            throw new LLMException(ErrorType.LLM_REQUEST_ERROR);
        } finally {
            if (gen != null) {
                // 返还池对象
                generationPool.returnObject(gen);
            }
        }

        return result;
    }

    private String buildPrompt(String input) {
        // 构造提示词
        DashVectorClient client = null;
        String result;
        try {
            client = dashVectorClientPool.borrowObject();
            DashVectorCollection collection = client.get(DashVectorUtil.COLLECTION_NAME);
            List<String> contents = DashVectorUtil.queryVectorNewsContent(input, 10, collection, apiKey);
            result = String.join("\n", contents) + "\n" + PROMPT + input;
        } catch (Exception e) {
            throw new LLMException(ErrorType.DASHVECTOR_ERROR);
        } finally {
            if (client != null) {
                dashVectorClientPool.returnObject(client);
            }
        }
        return result;
    }
}
