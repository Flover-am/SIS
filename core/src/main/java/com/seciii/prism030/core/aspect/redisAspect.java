package com.seciii.prism030.core.aspect;

import com.seciii.prism030.core.dao.news.NewsDAOMongo;
import com.seciii.prism030.core.enums.CategoryType;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.service.impl.RedisService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @auther: LiDongSheng
 * @date: 2024.4.15
 * redis切面，切新闻增删改操作
 */
@Aspect
@Component
public class redisAspect {
    private final RedisService redisService;

    private final NewsDAOMongo newsDAOMongo;

    @Autowired
    public redisAspect(RedisService redisService, NewsDAOMongo newsDAOMongo) {
        this.redisService = redisService;
        this.newsDAOMongo = newsDAOMongo;
    }


    /**
     * 修改新闻后更新redis的最后修改时间
     */
    @AfterReturning("@annotation(com.seciii.prism030.core.aspect.annotation.Modified)")
    public void afterSuccessModify() {
        redisService.modified();
    }


    /**
     * 添加新闻后更新redis的新闻数量，提取参数,并调用afterAdd方法
     */
    @AfterReturning(value = "execution(* com.seciii.prism030.core.service.impl.NewsServiceMongoImpl.addNews(..))")
    public void afterAdd(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        NewNews newNews = (NewNews) args[0];
        redisService.addNews(CategoryType.of(newNews.getCategory()).toInt());
    }


    /**
     * 删除新闻后更新redis的新闻数量，提取参数,并调用afterDelete方法
     */
    @AfterReturning(value = "execution(* com.seciii.prism030.core.service.impl.NewsServiceMongoImpl.deleteNews(..))")
    public void afterDelete(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        long id = (long) args[0];
        redisService.deleteNews(newsDAOMongo.getNewsById(id).getCategory());
    }

    /**
     * 删除多个新闻后更新redis的新闻数量，提取参数,并调用afterDelete方法
     */
    @AfterReturning(value = "execution(* com.seciii.prism030.core.service.impl.NewsServiceMongoImpl.deleteMultipleNews(..))")
    public void afterDeleteMultipleNews(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        List<Long> idList = (List<Long>) args[0];
        for (long id : idList) {
            redisService.deleteNews(newsDAOMongo.getNewsById(id).getCategory());
        }
    }
}
