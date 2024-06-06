package com.seciii.prism030.core.service;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.seciii.prism030.core.pojo.vo.news.TimelineUnitVO;
import io.reactivex.Flowable;

import java.util.List;

/**
 * @author xueruichen
 * @date 2024.05.27
 */
public interface LLMService {
    /**
     * 获取大模型输出
     *
     * @param input 用户输入
     * @return 输出的流式结果
     */
    Flowable<GenerationResult> getResult(String input);

    /**
     * 获取时间轴
     *
     * @param input 用户输入
     * @return 输出的流式结果
     */
    List<TimelineUnitVO> getTimeline(String input);
}
