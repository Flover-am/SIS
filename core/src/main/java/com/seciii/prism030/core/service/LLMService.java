package com.seciii.prism030.core.service;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.seciii.prism030.core.pojo.vo.llm.ReliabilityVO;
import com.seciii.prism030.core.pojo.vo.llm.TimelineUnitVO;
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
     * 获取大模型数据提问输出
     *
     * @param input 用户输入
     * @return 输出的流式结果
     */
    Flowable<GenerationResult> getDataResult(String input);

    /**
     * 获取时间轴
     *
     * @param input 用户输入
     * @return 输出的流式结果
     */
    List<TimelineUnitVO> getTimeline(String input);

    /**
     * 获取对应新闻的可信度
     *
     * @param newsId 新闻id
     * @return 可信度
     */
    ReliabilityVO getReliability(Long newsId);
}
