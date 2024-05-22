package com.seciii.prism030.core.utils;

import com.alibaba.dashscope.embeddings.TextEmbedding;
import com.alibaba.dashscope.embeddings.TextEmbeddingParam;
import com.alibaba.dashscope.embeddings.TextEmbeddingResult;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.aliyun.dashvector.DashVectorCollection;
import com.aliyun.dashvector.models.Doc;
import com.aliyun.dashvector.models.Vector;
import com.aliyun.dashvector.models.requests.QueryDocRequest;
import com.aliyun.dashvector.models.responses.Response;
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
    /**
     * 搜索相似向量对应的id
     *
     * @param query 搜索关键词
     * @param topK 需要搜索的条数
     * @param collection 集合
     * @return 搜索到的新闻id
     */
    public static List<Long> queryVector(String query, int topK, DashVectorCollection collection) {
        TextEmbedding textEmbedding = new TextEmbedding();
        TextEmbeddingResult result;
        try {
            result = textEmbedding.call(TextEmbeddingParam.builder()
                    .model(TextEmbedding.Models.TEXT_EMBEDDING_V1)
                    .text(query)
                    .apiKey("")
                    .build());
        } catch (NoApiKeyException e) {
            throw new RuntimeException(e);
        }

        if (result == null || result.getOutput().getEmbeddings().size() != 1) {
            throw new NewsException(ErrorType.NEWS_QUERY_ERROR);
        }

        Vector vector = Vector.builder().value(result.getOutput().getEmbeddings().get(0).getEmbedding()).build();
        QueryDocRequest request = QueryDocRequest.builder()
                .vector(vector)
                .topk(topK)
                .build();
        Response<List<Doc>> response = collection.query(request);

        if (!response.isSuccess()) {
            throw new NewsException(ErrorType.NEWS_QUERY_ERROR);
        }

        List<Long> idList = response.getOutput().stream().map(doc -> (Long) doc.getFields().get("newsId")).toList();
        return removeDuplicate(idList);
    }

    private static List<Long> removeDuplicate(List<Long> list) {
        Set<Long> set = new LinkedHashSet<>(list);
        return new ArrayList<>(set);
    }
}
