package com.seciii.prism030.core.config;

import com.seciii.prism030.core.aspect.RedisAspect;
import com.seciii.prism030.core.dao.news.NewsDAOMongo;
import com.seciii.prism030.core.service.SummaryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AOPConfig {

    @Bean
    RedisAspect redisAspect(SummaryService summaryService, NewsDAOMongo newsDAOMongo) {
        return new RedisAspect(summaryService, newsDAOMongo);
    }
}
