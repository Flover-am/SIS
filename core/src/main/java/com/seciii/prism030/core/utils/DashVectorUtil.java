package com.seciii.prism030.core.utils;

import com.alibaba.dashscope.embeddings.TextEmbedding;
import com.alibaba.dashscope.embeddings.TextEmbeddingParam;
import com.alibaba.dashscope.embeddings.TextEmbeddingResult;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.aliyun.dashvector.DashVectorClient;
import com.aliyun.dashvector.DashVectorCollection;
import com.aliyun.dashvector.models.Doc;
import com.aliyun.dashvector.models.Vector;
import com.aliyun.dashvector.models.requests.QueryDocRequest;
import com.aliyun.dashvector.models.responses.Response;
import com.seciii.prism030.common.exception.LLMException;
import com.seciii.prism030.common.exception.NewsException;
import com.seciii.prism030.common.exception.error.ErrorType;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * DashVector工具类
 *
 * @author xueruichen
 * @date 2024.05.22
 */
public class DashVectorUtil {
    public static final String COLLECTION_NAME = "sport_news_embedings";
    public static DashVectorCollection getCollection(DashVectorClient client) {
        return client.get(COLLECTION_NAME);
    }

    public static List<Doc> queryVectorDoc(String query, int topK, DashVectorCollection collection, String apiKey) {
        TextEmbedding textEmbedding = new TextEmbedding();
        TextEmbeddingResult result;
        try {
            result = textEmbedding.call(TextEmbeddingParam.builder()
                    .model(TextEmbedding.Models.TEXT_EMBEDDING_V1)
                    .text(query)
                    .apiKey(apiKey)
                    .build());
        } catch (NoApiKeyException e) {
            throw new LLMException(ErrorType.LLM_REQUEST_ERROR, e.getMessage());
        }

        if (result == null || result.getOutput().getEmbeddings().size() != 1) {
            throw new NewsException(ErrorType.NEWS_QUERY_ERROR);
        }

        Vector vector = Vector.builder().value(result.getOutput().getEmbeddings().get(0).getEmbedding().stream()
                .map(Double::floatValue)
                .toList()).build();
        QueryDocRequest request = QueryDocRequest.builder()
                .vector(vector)
                .topk(topK)
                .build();
        Response<List<Doc>> response = collection.query(request);

        if (!response.isSuccess()) {
            throw new NewsException(ErrorType.NEWS_QUERY_ERROR);
        }

        return response.getOutput();
    }

    public static List<Long> removeDuplicate(List<Long> list) {
        Set<Long> set = new LinkedHashSet<>(list);
        return new ArrayList<>(set);
    }
}
