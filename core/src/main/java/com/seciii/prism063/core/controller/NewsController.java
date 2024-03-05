package com.seciii.prism063.core.controller;

import com.seciii.prism063.common.Result;
import com.seciii.prism063.core.pojo.vo.news.NewsItemVO;
import com.seciii.prism063.core.pojo.vo.news.NewsVO;
import com.seciii.prism063.core.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 新闻控制器
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@RestController
@RequestMapping("/v1/")
public class NewsController {
    @Autowired
    private NewsService newsService;
    @GetMapping("news")
    public Result<List<NewsItemVO>> getNewsList(){
        List<NewsItemVO> newsList = newsService.getNewsList();
        return Result.success(newsList);
    }
    @GetMapping("news/page")
    public Result<List<NewsItemVO>> getNewsListByPage(Integer pageNo,Integer pageSize){
        List<NewsItemVO> newsList = newsService.getNewsListByPage(pageNo,pageSize);
        return Result.success(newsList);
    }
    @GetMapping("news/{id}")
    public Result<NewsVO> getNewsDetail(@PathVariable Long id){
        NewsVO newsVO= newsService.getNewsDetail(id);
        return Result.success(newsVO);
    }
}
