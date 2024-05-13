package com.seciii.prism030.core.rabbitmq;

import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.pojo.vo.news.NewsVO;
import com.seciii.prism030.core.service.NewsService;
import com.seciii.prism030.core.service.impl.NewsServiceMongoImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * RabbitMQ消费者类，实现获取并处理消息队列中的信息，存入mongoDB
 *
 * @author windloong
 */
@Component
@RabbitListener(queues = "news_queue")
@Slf4j
public class RabbitConsumer {
    NewsService newsService;



    /**
     * RabbitConsumer构造函数，注入NewsServiceMongoImpl实例
     *
     * @param newsServiceMongo NewsServiceMongoImpl实例
     */
    public RabbitConsumer(NewsServiceMongoImpl newsServiceMongo) {
        this.newsService = newsServiceMongo;
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
        NewNews newNews = MessageConvertor.parseJsonToNewNews(jsonString);

        // 将newNews对象添加到newsServiceMongo中
        long newsId = newsService.addNews(newNews);
        NewsVO newsVO = newsService.getNewsDetail(newsId);

        if (newsVO == null) {
            log.error(String.format("Failed to get news detail after adding news, id: %d.", newsId));
            return;
        }

    }
}
