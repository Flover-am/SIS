package com.seciii.prism030.core.rabbitmq;

import com.seciii.prism030.core.mapper.news.VectorNewsMapper;
import com.seciii.prism030.core.pojo.dto.NewsEntityRelationshipDTO;
import com.seciii.prism030.core.pojo.po.news.VectorNewsPO;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.pojo.vo.news.NewsVO;
import com.seciii.prism030.core.service.GraphService;
import com.seciii.prism030.core.service.NewsService;
import com.seciii.prism030.core.service.impl.NewsServiceMongoImpl;
import com.seciii.prism030.core.utils.RabbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * RabbitMQ消费者类，实现获取并处理消息队列中的信息，存入mongoDB
 *
 * @author windloong
 */
@Component
@RabbitListener(queues = "news_queue")
@Slf4j
public class RabbitConsumer {
    private final VectorNewsMapper vectorNewsMapper;

    private final NewsService newsService;

    private final GraphService graphService;

    /**
     * RabbitConsumer构造函数，注入NewsServiceMongoImpl实例
     *
     * @param newsServiceMongo NewsServiceMongoImpl实例
     */
    public RabbitConsumer(NewsServiceMongoImpl newsServiceMongo, VectorNewsMapper vectorNewsMapper, GraphService graphService) {
        this.newsService = newsServiceMongo;
        this.vectorNewsMapper = vectorNewsMapper;
        this.graphService = graphService;
    }

    /**
     * 处理接收到的消息
     *
     * @param data 接收到的消息数据，以字节数组形式传入
     */
    @RabbitHandler
    public void process(byte[] data) {
        // 将字节数组转换成UTF-8编码的字符串
        String jsonString = new String(data, StandardCharsets.UTF_8);

        // 使用自定义的MessageConvertor将JSON字符串解析为NewNews对象
        NewNews newNews = RabbitUtil.parseJsonToNewNews(jsonString);

        // 将newNews对象添加到newsServiceMongo中
        long newsId = newsService.addNews(newNews);
        NewsVO newsVO = newsService.getNewsDetail(newsId);

        if (newsVO == null) {
            log.error(String.format("Failed to get news detail after adding news, id: %d.", newsId));
            return;
        }

        for (String vectorId : newNews.getDashId()) {
            VectorNewsPO vectorNews = VectorNewsPO.builder()
                    .vectorId(vectorId)
                    .newsId(newsId)
                    .build();
            vectorNewsMapper.insert(vectorNews);
        }

        //插入词云
        List<String> wordSegment = RabbitUtil.getWordSegment(jsonString);
        newsService.saveWordCloud(newsId, wordSegment);

        // 插入新闻的实体关系
        List<NewsEntityRelationshipDTO> erList = RabbitUtil.getERList(jsonString);
        graphService.addNewsNode(newsId, newNews.getTitle());
        graphService.addNewsEntities(newsId, erList);
    }
}
