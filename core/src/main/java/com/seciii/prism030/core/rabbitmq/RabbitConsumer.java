package com.seciii.prism030.core.rabbitmq;

import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.service.impl.NewsServiceMongoImpl;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.message.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RabbitListener(queues = "news_queue")
public class RabbitConsumer {
    NewsServiceMongoImpl newsServiceMongo;

    public RabbitConsumer(NewsServiceMongoImpl newsServiceMongo){
        this.newsServiceMongo = newsServiceMongo;
    }

    @RabbitHandler
    public void process(byte[] data) {
        String jsonString = new String(data, StandardCharsets.UTF_8);
        String fullString = StringEscapeUtils.unescapeJava(jsonString);
//        System.out.println(fullString);
        NewNews newNews = MessageConvertor.parseJsonToNewNews(fullString);


        newsServiceMongo.addNews(newNews);

    }
}
