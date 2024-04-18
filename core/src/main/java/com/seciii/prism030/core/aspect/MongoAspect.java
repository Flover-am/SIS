package com.seciii.prism030.core.aspect;

import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Mongo切面
 *
 * @author wang mingsong
 * @date 2024.4.18
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class MongoAspect {
    private NewsService newsService;

    @Autowired
    public void setNewsService(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * 添加新闻后生成词云
     * @param joinPoint
     * @return
     */
    @Around("@annotation(com.seciii.prism030.core.aspect.annotation.Add)")
    public Object aroundAddNews(ProceedingJoinPoint joinPoint) {
        NewNews newNews = (NewNews) joinPoint.getArgs()[0];
        Long newsId = null;
        System.err.println("---MongoAspect.aroundAddNews---");
        try {
            newsId = (Long) joinPoint.proceed();
            newsService.generateAndSaveWordCloud(newsId, newNews.getContent());
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
        }
        return newsId;
    }
}
