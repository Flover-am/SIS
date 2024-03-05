package com.seciii.prism063.core.controller;

import com.seciii.prism063.common.Result;
import com.seciii.prism063.core.pojo.vo.news.NewsItemVO;
import com.seciii.prism063.core.pojo.vo.news.NewsVO;
import com.seciii.prism063.core.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 获取新闻列表
     * @return 新闻条目VO列表
     */
    @GetMapping("news")
    public Result<List<NewsItemVO>> getNewsList(){
        List<NewsItemVO> newsList = newsService.getNewsList();
        return Result.success(newsList);
    }

    /**
     * 按页数和页大小获取新闻列表
     * @param pageNo 页码下标
     * @param pageSize 页大小
     * @return 对应页数新闻条目VO列表
     */
    @GetMapping("news/page")
    public Result<List<NewsItemVO>> getNewsListByPage(Integer pageNo,Integer pageSize){
        List<NewsItemVO> newsList = newsService.getNewsListByPage(pageNo,pageSize);
        return Result.success(newsList);
    }

    /**
     * 获取新闻详情
     * @param id 新闻id
     * @return 新闻VO
     */
    @GetMapping("news/{id}")
    public Result<NewsVO> getNewsDetail(@PathVariable Long id){
        NewsVO newsVO= newsService.getNewsDetail(id);
        return Result.success(newsVO);
    }

    /**
     * 删除新闻
     * @param id 新闻id
     * @return 删除结果，成功则message为"success"
     */
    @DeleteMapping("news/{id}")
    public Result<Void> deleteNews(@PathVariable Long id){
        newsService.deleteNews(id);
        return Result.success();
    }
}
