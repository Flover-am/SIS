package com.seciii.prism030.core.aspect;

import com.seciii.prism030.core.dao.news.NewsDAOMongo;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.service.SummaryService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author LiDongSheng
 * @date 2024.4.15
 * redis切面，切新闻增删改操作
 */
@Aspect
@Slf4j
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
        summaryService.modify();
    }


    /**
     * 添加新闻后更新redis的新闻数量，提取参数,并调用afterAdd方法
     */
    @AfterReturning(value = "execution(* com.seciii.prism030.core.service.impl.NewsServiceMongoImpl.addNews(..))", returning = "methodResult")
    public void afterAdd(JoinPoint joinPoint, Object methodResult) {
        long id = (long) methodResult;
        Object[] args = joinPoint.getArgs();
        NewNews newNews = (NewNews) args[0];

        summaryService.addNews(newNews);
    }


    /**
     * 删除新闻后更新redis的新闻数量，提取参数,并调用afterDelete方法
     */
    @Around(value = "execution(* com.seciii.prism030.core.service.impl.NewsServiceMongoImpl.deleteNews(..))")
    public Object afterDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        long id = (long) args[0];
        int category = newsDAOMongo.getNewsById(id).getCategory();
        try {
            Object result = joinPoint.proceed();
            summaryService.deleteNews(category);
            return result;
        } catch (Throwable e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 删除多个新闻后更新redis的新闻数量，提取参数,并调用afterDelete方法
     */
    @Around(value = "execution(* com.seciii.prism030.core.service.impl.NewsServiceMongoImpl.deleteMultipleNews(..))")
    public Object afterDeleteMultipleNews(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        List<Long> idList = (List<Long>) args[0];
        List<Integer> categoryList = idList.stream().map(id -> newsDAOMongo.getNewsById(id).getCategory()).toList();
        try {
            Object result = joinPoint.proceed();
            for (int category : categoryList) {
                summaryService.deleteNews(category);
            }
            return result;
        } catch (Throwable e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
