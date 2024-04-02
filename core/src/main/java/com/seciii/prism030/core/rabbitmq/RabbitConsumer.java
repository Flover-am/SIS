package com.seciii.prism030.core.rabbitmq;

import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.service.impl.NewsServiceMongoImpl;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "news_queue")
public class RabbitConsumer {
    NewsServiceMongoImpl newsServiceMongo;

    public RabbitConsumer(NewsServiceMongoImpl newsServiceMongo){
        this.newsServiceMongo = newsServiceMongo;
    }

    @RabbitHandler
    public void process(String msg) {
        NewNews newNews = MessageConvertor.parseJsonToNewNews(msg);


        newsServiceMongo.addNews(newNews);

        System.out.println("------------" + msg);
    }
}
