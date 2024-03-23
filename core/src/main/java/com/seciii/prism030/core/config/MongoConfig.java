package com.seciii.prism030.core.config;

import com.seciii.prism030.core.dao.news.NewsDAOMongo;
import com.seciii.prism030.core.dao.news.impl.NewsDAOMongoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * MongoDB配置类
 * 声明所需的Bean
 *
 * @author wang mingsong
 * @date 2024.03.22
 */
@Configuration
public class MongoConfig {
    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTemplate(mongoDatabaseFactory);
    }

    @Bean
    public NewsDAOMongo newsDAOMongo() {
        return new NewsDAOMongoImpl();
    }
}
