package com.seciii.prism030.core.aspect;

import com.seciii.prism030.core.dao.news.NewsDAOMongo;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.service.SummaryService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author LiDongSheng
 * @date 2024.4.15
 * redis切面，切新闻增删改操作
 */
@Aspect
public class RedisAspect {
    private final SummaryService summaryService;

    private final NewsDAOMongo newsDAOMongo;

    @Autowired
    public RedisAspect(SummaryService summaryService, NewsDAOMongo newsDAOMongo) {
        this.summaryService = summaryService;
        this.newsDAOMongo = newsDAOMongo;
    }


    /**
     * 修改新闻后更新redis的最后修改时间
     */
    @AfterReturning("@annotation(com.seciii.prism030.core.aspect.annotation.Modified)")
    public void afterSuccessModify() {
        summaryService.modified();
    }


    /**
     * 添加新闻后更新redis的新闻数量，提取参数,并调用afterAdd方法
     */
    @AfterReturning(value = "execution(* com.seciii.prism030.core.service.impl.NewsServiceMongoImpl.addNews(..))", returning = "methodResult")
    public void afterAdd(JoinPoint joinPoint, Object methodResult) {
        long id = (long) methodResult;
        Object[] args = joinPoint.getArgs();
        NewNews newNews = (NewNews) args[0];

        summaryService.addNews(newsDAOMongo.getNewsById(id).getCategory());
    }


    /**
     * 删除新闻后更新redis的新闻数量，提取参数,并调用afterDelete方法
     */
    @AfterReturning(value = "execution(* com.seciii.prism030.core.service.impl.NewsServiceMongoImpl.deleteNews(..))")
    public void afterDelete(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        long id = (long) args[0];
        summaryService.deleteNews(newsDAOMongo.getNewsById(id).getCategory());
    }

    /**
     * 删除多个新闻后更新redis的新闻数量，提取参数,并调用afterDelete方法
     */
    @AfterReturning(value = "execution(* com.seciii.prism030.core.service.impl.NewsServiceMongoImpl.deleteMultipleNews(..))")
    public void afterDeleteMultipleNews(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        List<Long> idList = (List<Long>) args[0];
        for (long id : idList) {
            summaryService.deleteNews(newsDAOMongo.getNewsById(id).getCategory());
        }
    }
}
