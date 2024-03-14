package com.seciii.prism063.core.controller;

import com.seciii.prism063.common.Result;
import com.seciii.prism063.core.pojo.dto.PagedNews;
import com.seciii.prism063.core.pojo.vo.news.Filter;
import com.seciii.prism063.core.pojo.vo.news.NewNews;
import com.seciii.prism063.core.pojo.vo.news.NewsVO;
import com.seciii.prism063.core.service.NewsService;
import com.seciii.prism063.core.utils.DateTimeUtil;
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

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * 获取新闻列表
     *
     * @return 新闻条目VO列表
     */
    @GetMapping("/news")
    @Deprecated
    public Result<PagedNews> getNewsList() {
        PagedNews pagedNews = newsService.getNewsList();
        return Result.success(pagedNews);
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
    @GetMapping("/news/all")
    @Deprecated
    public Result<PagedNews> getNewsListByPage(@RequestParam Integer current, @RequestParam Integer pageSize) {
        PagedNews pagedNews = newsService.getNewsListByPage(current, pageSize);
        return Result.success(pagedNews);
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
     * @param current   当前页码
     * @param pageSize  页大小
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param category  分类
     * @return 对应页数新闻条目VO列表
     */
    @PostMapping("/news/filter")
    public Result<PagedNews> filterNewsPaged(
            @RequestParam int current,
            @RequestParam int pageSize,
            @RequestBody(required = false) Filter filter) {
        PagedNews pagedNews = newsService.filterNewsPaged(current, pageSize,
                filter.getCategory(),
                DateTimeUtil.parseBeginOfDay(filter.getStartDate()),
                DateTimeUtil.parseEndOfDay(filter.getEndDate())
        );
        return Result.success(pagedNews);
    }

    /**
     * 按标题模糊搜索新闻并按过滤器过滤，以分页方式返回
     *
     * @param current   当前页码
     * @param pageSize  页大小
     * @param query     搜索关键词
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param category  分类
     * @return 新闻条目VO列表
     */
    @PostMapping("/news/search")
    public Result<PagedNews> searchNewsByTitle(
            @RequestParam int current,
            @RequestParam int pageSize,
            @RequestParam String query,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestBody(required = false) List<String> category) {
        PagedNews pagedNews = newsService.searchNewsByTitleFiltered(current, pageSize, query,
                category,
                DateTimeUtil.parseBeginOfDay(startDate),
                DateTimeUtil.parseEndOfDay(endDate)
        );
        return Result.success(pagedNews);
    }

    /**
     * 更改新闻全部信息
     *
     * @param id 新闻id
     */
    @PostMapping("/news/modify/{id}")
    public Result<Void> modifyNews(
            @PathVariable Long id,
            @RequestParam(required = false) String newTitle,
            @RequestParam(required = false) String newContent,
            @RequestParam(required = false) String newOriginSource
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

    /**
     * 按来源搜索新闻
     *
     * @param
     * @return
     */
//    @PostMapping("/news/searchSource")
//    public Result<PagedNews> searchNewsBySource(@RequestParam int current,
//                                                @RequestParam int pageSize,
//                                                @RequestParam String query,
//                                                @RequestParam(required = false) String startDate,
//                                                @RequestParam(required = false) String endDate,
//                                                @RequestBody(required = false) List<String> category){
//        PagedNews pagedNews = newsService.searchNewsBySourceFiltered(current, pageSize, query,
//                category,
//                DateTimeUtil.parseBeginOfDay(startDate),
//                DateTimeUtil.parseEndOfDay(endDate)
//        );
//        return Result.success(pagedNews);
//    }


    /**
     * 批量删除新闻
     *
     * @param idList 新闻id列表
     * @return 删除结果，成功则message为"success"
     */
    @DeleteMapping("/news/delete-multiple")
    public Result<Void> deleteMultiple(@RequestBody List<Long> idList) {
        newsService.deleteMultipleNews(idList);
        return Result.success();
    }
}
