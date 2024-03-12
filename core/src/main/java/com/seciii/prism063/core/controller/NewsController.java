package com.seciii.prism063.core.controller;

import com.seciii.prism063.common.Result;
import com.seciii.prism063.core.pojo.dto.Filter;
import com.seciii.prism063.core.pojo.vo.news.NewNews;
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
@RequestMapping("/v1")
public class NewsController {
    @Autowired
    private NewsService newsService;

    /**
     * 获取新闻列表
     *
     * @return 新闻条目VO列表
     */
    @GetMapping("/news")
    public Result<List<NewsItemVO>> getNewsList() {
        List<NewsItemVO> newsList = newsService.getNewsList();
        return Result.success(newsList);
    }

    /**
     * 新增新闻
     */
    @PostMapping("/news")
    public Result<Void> addNews(@RequestBody NewNews newNews) {
        newsService.addNews(newNews);
        return Result.success();
    }

    /**
     * 按页数和页大小获取新闻列表
     *
     * @param current  页码下标
     * @param pageSize 页大小
     * @return 对应页数新闻条目VO列表
     */
    @GetMapping("/news/page")
    public Result<List<NewsItemVO>> getNewsListByPage(@RequestParam Integer current, @RequestParam Integer pageSize) {
        List<NewsItemVO> newsList = newsService.getNewsListByPage(current, pageSize);
        return Result.success(newsList);
    }

    /**
     * 获取新闻详情
     *
     * @param id 新闻id
     * @return 新闻VO
     */
    @GetMapping("/news/{id}")
    public Result<NewsVO> getNewsDetail(@PathVariable Long id) {
        NewsVO newsVO = newsService.getNewsDetail(id);
        return Result.success(newsVO);
    }

    /**
     * 修改新闻标题
     *
     * @param id    新闻id
     * @param title 新标题
     * @return 修改结果，成功则message为"success"
     */
    @PutMapping("/news/modify/{id}/title")
    public Result<Void> modifyNewsTitle(@PathVariable Long id, @RequestParam String title) {
        newsService.modifyNewsTitle(id, title);
        return Result.success();
    }

    /**
     * 修改新闻内容
     *
     * @param id      新闻id
     * @param content 新内容
     * @return 修改结果，成功则message为"success"
     */
    @PutMapping("/news/modify/{id}/content")
    public Result<Void> modifyNewsContent(@PathVariable Long id, @RequestParam String content) {
        newsService.modifyNewsContent(id, content);
        return Result.success();
    }

    /**
     * 修改新闻来源
     *
     * @param id     新闻id
     * @param source 新来源
     * @return 修改结果，成功则message为"success"
     */
    @PutMapping("/news/modify/{id}/source")
    public Result<Void> modifyNewsSource(@PathVariable Long id, @RequestBody String source) {
        newsService.modifyNewsSource(id, source);
        return Result.success();
    }

    /**
     * 删除新闻
     *
     * @param id 新闻id
     * @return 删除结果，成功则message为"success"
     */
    @DeleteMapping("/news/{id}")
    public Result<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return Result.success();
    }

    /**
     * 分页筛选新闻
     *
     * @param current  当前页码
     * @param pageSize 页大小
     * @param filter   筛选条件
     * @return 对应页数新闻条目VO列表
     */
    @GetMapping("/news/filter")
    public Result<List<NewsItemVO>> filterNewsPaged(
            @RequestParam int current,
            @RequestParam int pageSize,
            @RequestBody Filter filter) {
        List<NewsItemVO> newsList = newsService.filterNewsPaged(current, pageSize,
                filter.getCategory(),
                filter.getDateRange().getStart(),
                filter.getDateRange().getEnd()
        );
        return Result.success(newsList);
    }

    /**
     * 按标题模糊搜索新闻
     *
     * @param titleString 搜索字符串
     * @return 新闻条目VO列表
     */
    @GetMapping("/news/search")
    public Result<List<NewsItemVO>> searchNewsByTitle(@RequestParam String titleString) {
        List<NewsItemVO> newsList = newsService.searchNewsByTitle(titleString);
        return Result.success(newsList);
    }

    /**
     * 更改新闻全部信息
     *
     * @param id 新闻id
     */
    @PostMapping("/news/modify/{id}")
    public Result<Void> modifyNews(
            @PathVariable Long id,
            @RequestParam String newTitle,
            @RequestParam String newContent,
            @RequestParam String newOriginSource
    ) {
        if (newTitle != null && !newTitle.isEmpty()) {
            newsService.modifyNewsTitle(id, newTitle);
        }
        if (newContent != null && !newContent.isEmpty()) {
            newsService.modifyNewsContent(id, newContent);
        }
        if (newOriginSource != null && !newOriginSource.isEmpty()) {
            newsService.modifyNewsSource(id, newOriginSource);
        }
        return Result.success();
    }
}
