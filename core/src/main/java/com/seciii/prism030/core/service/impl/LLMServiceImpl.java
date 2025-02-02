package com.seciii.prism030.core.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.aliyun.dashvector.DashVectorClient;
import com.aliyun.dashvector.DashVectorCollection;
import com.aliyun.dashvector.models.Doc;
import com.seciii.prism030.common.exception.LLMException;
import com.seciii.prism030.common.exception.NewsException;
import com.seciii.prism030.common.exception.error.ErrorType;
import com.seciii.prism030.core.config.poolconfig.pool.DashVectorClientPool;
import com.seciii.prism030.core.config.poolconfig.pool.GenerationPool;
import com.seciii.prism030.core.dao.news.NewsDAOMongo;
import com.seciii.prism030.core.mapper.news.VectorNewsMapper;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.po.news.VectorNewsPO;
import com.seciii.prism030.core.pojo.vo.llm.ReliabilityVO;
import com.seciii.prism030.core.pojo.vo.llm.TimelineUnitVO;
import com.seciii.prism030.core.pojo.vo.news.NewsCategoryCountVO;
import com.seciii.prism030.core.pojo.vo.news.NewsDateCountVO;
import com.seciii.prism030.core.service.LLMService;
import com.seciii.prism030.core.utils.DashScopeUtil;
import com.seciii.prism030.core.utils.DashVectorUtil;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 大模型服务实现类
 *
 * @author xueruichen
 * @date 2024.05.27
 */
@Service
public class LLMServiceImpl implements LLMService {
    @Value("${dashscope.apikey}")
    private String apiKey;
    private final GenerationPool generationPool;
    private final DashVectorClientPool dashVectorClientPool;
    private final VectorNewsMapper vectorNewsMapper;
    private final NewsDAOMongo newsDAOMongo;
    private final NewsServiceMongoImpl newsServiceMongo;
    private static final Integer STATISTIC_DAYS = 7;
    private static final String STATISTIC_PROMPT =
            """
            今天的日期是%s。
            下面是一个系统的数据，代表近7天内每种分类在某天增加的新闻数量：
            %s
            请根据以上信息，回答我的问题。回答问题时，所有的分类都要用中文名称。
            我的问题为：
            """;
    private static final String QA_PROMPT =
            """
            请根据以上信息，回答我的问题，如果能找到确切的信息来源，在答案后以url的格式附上你的推断来源。url格式:http://139.224.40.88:82/detail?id={id}，将{id}替换为每段新闻前附上的实际id。如果问题跟信息无关，就不用加上url了。
            请按照markdown的格式返回结果，如示例：
            - 孙杨结束禁赛 [参考1](http://139.224.40.88:82/detail?id=1)
            - 皇马赢得冠军 [参考2](http://139.224.40.88:82/detail?id=2)
            以上是示例内容。
            我的问题为：
            """;

    private static final String TIMELINE_PROMPT =
            """
            你是一名提取善于分析事件的时间顺序的专家。请根据以上信息，将文本中涉及到的主要事件提取出来，并按照(时间 | 事件 | id)的格式输出，输出顺序为时间顺序。其中，时间的格式为"yyyy-MM-dd"，如"2024-05-22"，id为每段新闻前附上的实际id。对于不确定时间的事件，请将时间设置为NULL。
            下面是一个输出的例子：
            (2024-05-02 | 第十一届“亲相见杯”乒乓球俱乐部联盟赛在山东威海南海国家乒乓球训练基地举行 | 13)
            (2024-05-05 | 第十一届“亲相见杯”乒乓球俱乐部联盟赛结束 | 13)
            (NULL | 张宸睿在丁组混合单打比赛中勇夺冠军 | 15)
            (NULL | 张宸睿孪生哥哥张宸硕获得该组别第4名 | 16)
            例子结束。
            如果没有找到任何和信息相关的时间轴顺序，就直接返回NULL，不要输出其他内容。
            下面是我的输入：
            """;

    private static final String RELIABILITY_PROMPT =
            """
            请根据互联网搜索内容，判断下面这段信息的可信度。将可信度分为0，1，2，3，4五个等级，可信度依次升高，分别代表“完全不可信”，“比较不可信”，“中等”，“比较可信”，“非常可信”。
            请输出可信度等级，并说明你的判断依据，格式为：
            (可信度等级 | 判断依据)
            举个例子：
            我的信息为：
            马龙是羽毛球运动员。
            你的回答为：
            (0 | 根据相关资料，马龙是一位乒乓球运动员，而不是羽毛球运动员)
            例子结束。
            下面是我的输入内容：
            """;


    public LLMServiceImpl(GenerationPool generationPool, DashVectorClientPool dashVectorClientPool, VectorNewsMapper vectorNewsMapper, NewsDAOMongo newsDAOMongo, NewsServiceMongoImpl newsServiceMongo) {
        this.generationPool = generationPool;
        this.dashVectorClientPool = dashVectorClientPool;
        this.vectorNewsMapper = vectorNewsMapper;
        this.newsDAOMongo = newsDAOMongo;
        this.newsServiceMongo = newsServiceMongo;
    }

    @Override
    public Flowable<GenerationResult> getResult(String input) {
        Generation gen = null;
        Flowable<GenerationResult> result;
        String prompt = buildPrompt(QA_PROMPT, input);
        try {
            // 从池中取对象
            gen = generationPool.borrowObject();
            result = DashScopeUtil.streamChat(apiKey, prompt, gen);
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

    @Override
    public Flowable<GenerationResult> getDataResult(String input) {
        Generation gen = null;
        Flowable<GenerationResult> result;
        String prompt = dataPrompt(STATISTIC_DAYS) + input;
        try {
            // 从池中取对象
            gen = generationPool.borrowObject();
            result = DashScopeUtil.streamChat(apiKey, prompt, gen);
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

    @Override
    public List<TimelineUnitVO> getTimeline(String input) {
        Generation gen = null;
        GenerationResult result;
        String prompt = buildPrompt(TIMELINE_PROMPT, input);
        try {
            // 从池中取对象
            gen = generationPool.borrowObject();
            result = DashScopeUtil.chat(apiKey, prompt, gen, false);
        } catch (Exception e) {
            throw new LLMException(ErrorType.LLM_REQUEST_ERROR);
        } finally {
            if (gen != null) {
                // 返还池对象
                generationPool.returnObject(gen);
            }
        }
        String output = result.getOutput().getChoices().get(0).getMessage().getContent();
        return timelineMapper(output);
    }

    @Override
    public ReliabilityVO getReliability(Long newsId) {
        NewsPO news = newsDAOMongo.getNewsById(newsId);
        if (news == null) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }

        Generation gen = null;
        GenerationResult result;
        String prompt = RELIABILITY_PROMPT + news.getContent();
        try {
            // 从池中取对象
            gen = generationPool.borrowObject();
            result = DashScopeUtil.chat(apiKey, prompt, gen, true);
        } catch (Exception e) {
            throw new LLMException(ErrorType.LLM_REQUEST_ERROR);
        } finally {
            if (gen != null) {
                // 返还池对象
                generationPool.returnObject(gen);
            }
        }
        String output = result.getOutput().getChoices().get(0).getMessage().getContent();
        return reliabilityMapper(output);
    }


    private String buildPrompt(String prompt, String input) {
        // 构造提示词
        DashVectorClient client = null;
        String result;
        try {
            client = dashVectorClientPool.borrowObject();
            DashVectorCollection collection = client.get(DashVectorUtil.COLLECTION_NAME);
            List<String> contents = queryVectorNewsContent(input, 10, collection, apiKey);
            result = String.join("\n", contents) + "\n" + prompt + input;
        } catch (Exception e) {
            throw new LLMException(ErrorType.DASHVECTOR_ERROR);
        } finally {
            if (client != null) {
                dashVectorClientPool.returnObject(client);
            }
        }
        return result;
    }

    /**
     * 搜索相似向量对应的内容
     *
     * @param query 搜索关键词
     * @param topK 需要搜索的条数
     * @param collection 集合
     * @return 搜索到的新闻内容
     */
    public List<String> queryVectorNewsContent(String query, int topK, DashVectorCollection collection, String apiKey) {
        List<Doc> docs = DashVectorUtil.queryVectorDoc(query, topK, collection, apiKey);
        return docs.stream().map(doc -> {
            VectorNewsPO vectorNewsPO = vectorNewsMapper.getVectorNewsByVectorId(doc.getId());

            if (vectorNewsPO == null) {
                return "";
            }

            Long newsId = vectorNewsPO.getNewsId();
            return "newsId: " + newsId + "\n" + doc.getFields().get("text");
        }).toList();
    }

    private List<TimelineUnitVO> timelineMapper(String output) {
        if (!output.startsWith("(") || !output.endsWith(")")) {
            return new ArrayList<>();
        }
        // 将输出映射为时间轴数据结构
        String[] splits = output.split("\n");
        List<TimelineUnitVO> timeline = new ArrayList<>();
        for (String split : splits) {
            timeline.add(timelineUnitMapper(split));
        }
        return timeline;
    }

    private TimelineUnitVO timelineUnitMapper(String unit) {
        String strip = unit.strip().substring(1, unit.length() - 1);
        String[] elements = strip.split(" \\| ");
        if (elements.length != 3) {
            throw new LLMException(ErrorType.LLM_TIMELINE_RESPONSE_ERROR);
        }
        return TimelineUnitVO.builder()
                .event(elements[1])
                .time(elements[0])
                .newsId(elements[2])
                .build();
    }

    private ReliabilityVO reliabilityMapper(String unit) {
        String strip = unit.strip().substring(1, unit.length() - 1);
        String[] elements = strip.split(" \\| ");
        if (elements.length != 2) {
            throw new LLMException(ErrorType.LLM_RELIABILITY_RESPONSE_ERROR);
        }
        return ReliabilityVO.builder().reliability(Integer.parseInt(elements[0])).reason(elements[1]).build();
    }

    private String dataPrompt(int day) {
        LocalDate today = LocalDate.now();
        LocalDate begin = today.minusDays(day);
        List<NewsDateCountVO> newsDateCountVOS = newsServiceMongo.countPeriodNews(begin.toString(), today.toString());
        StringBuilder sb = new StringBuilder();
        for (NewsDateCountVO newsDateCountVO : newsDateCountVOS) {
            sb.append(newsDateCountVO.getDate())
                    .append(":\n");
            for (NewsCategoryCountVO newsCategoryCountVO : newsDateCountVO.getNewsCategoryCounts()) {
                sb.append(newsCategoryCountVO.getCategory()).append(": ")
                        .append(newsCategoryCountVO.getCount()).append("\n");
            }
            sb.append("\n");
        }
        return String.format(STATISTIC_PROMPT, today, sb);
    }
}
