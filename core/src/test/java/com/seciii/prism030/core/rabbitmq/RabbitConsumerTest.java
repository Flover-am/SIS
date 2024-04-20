package com.seciii.prism030.core.rabbitmq;

import com.seciii.prism030.core.enums.CategoryType;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.service.impl.NewsServiceMongoImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;

import java.nio.charset.StandardCharsets;
import static org.mockito.Mockito.*;

import com.seciii.prism030.core.rabbitmq.RabbitConsumer;
import com.seciii.prism030.core.service.impl.NewsServiceMongoImpl;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * RabbitMQ消费者测试类
 */
@ExtendWith(MockitoExtension.class)
public class RabbitConsumerTest {

    @Mock
    private NewsServiceMongoImpl newsServiceMongoMock;

    @InjectMocks
    private RabbitConsumer rabbitConsumer;

    @Test
    public void testProcess() {
        byte[] testData = "{\"title\": \"小米SU7拥有9种外观颜色、4款内饰，风阻系数为0.195\", \"category\": \"股市\", \"source_time\": \"2024-03-28 19:29\", \"link\": \"https://finance.sina.com.cn/tech/it/2024-03-28/doc-inapwqvs2233619.shtml\", \"origin_source\": \"新浪科技\", \"source_link\": \"\", \"content\": \"新浪科技讯 3月28日晚间消息，在今日的小米汽车上市发布会上，小米CEO雷军发表演讲。\\n　　雷军介绍，小米SU7拥有4大色系，9种颜色：\\n　　跑车色系：海湾蓝、熔岩橙\\n　　时尚色系：雅灰、流星蓝、霞光紫\\n　　豪华色系：橄榄绿、寒武岩灰\\n　　经典色系：珍珠白、钻石黑\\n　　另外，还有4款内饰、4套轮毂轮胎可供选择。\\n　　小米SU7的定位为C级豪华科技轿车，风阻系数为0.195。\\n责任编辑：刘万里 SF014\"}".getBytes(); // Replace this with your test data

        rabbitConsumer.process(testData);

        //校验是否成功调用addNews函数
        verify(newsServiceMongoMock).addNews(any(NewNews.class));
    }
}
