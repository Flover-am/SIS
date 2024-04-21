package com.seciii.prism030.core.controller;

import com.seciii.prism030.core.pojo.vo.news.*;
import com.seciii.prism030.common.Result;
import com.seciii.prism030.core.pojo.dto.PagedNews;
import com.seciii.prism030.core.service.NewsService;
import com.seciii.prism030.core.utils.DateTimeUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 新闻控制器.
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
     * 获取今日新闻数量与昨日新闻数量的差值
     *
     * @return 今日新闻数量与昨日新闻数量的差值
     */
    @GetMapping("/news/diffDays")
    public Result<Integer> diffTodayAndYesterday() {
        return Result.success(newsService.diffTodayAndYesterday());
    }

    /**
     * 今日每种新闻的新闻数量
     *
     * @return 今日每种新闻的新闻数量
     */
    @GetMapping("/news/countAllCategory")
    public Result<List<NewsCategoryCountVO>> countAllCategoryNews() {
        return Result.success(newsService.countAllCategoryNews());
    }

    /**
     * 获取一段时间内每天每种的新闻数量
     */
    @GetMapping("/news/countPeriod")
    public Result<List<NewsDateCountVO>> countPeriodNews(@RequestParam String startTime, @RequestParam String endTime) {
        return Result.success(newsService.countPeriodNews(startTime, endTime));
    }

    /**
     * 获取今日新闻数量
     *
     * @return 今日新闻数量
     */
    @GetMapping("/news/countDay")
    public Result<Integer> countDateNews() {
        return Result.success(newsService.countDateNews());
    }

    /**
     * 获取今日新闻分类数量
     */
    @GetMapping("/news/countCategory")
    public Result<Integer> countCategoryNews(@RequestParam int category) {
        return Result.success(newsService.countCategoryNews(category));
    }

    /**
     * 获取最后一次修改时间
     *
     * @return 最后一次修改时间
     */
    @GetMapping("/news/lastModify")
    public Result<String> lastModify() {
        return Result.success(newsService.getLastModified());
    }

    /**
     * 获取一周内新闻数量
     */
    @GetMapping("/news/countWeek")
    public Result<Integer> countWeekNews() {
        return Result.success(newsService.countWeekNews());
    }


    /**
     * 新增新闻
     *
     */
    @PostMapping("/news")
    public Result<Void> addNews(@RequestBody NewNews newNews) {
        newsService.addNews(newNews);
        return Result.success();
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
     * @param filter   过滤器
     * @return 对应页数新闻条目VO列表
     */
    @PostMapping("/news/filter")
    public Result<PagedNews> filterNewsPaged(@RequestParam int current, @RequestParam int pageSize,
                                             @RequestBody(required = false) Filter filter) {
        PagedNews pagedNews = newsService.filterNewsPaged(current, pageSize,
                filter.getCategory(),
                DateTimeUtil.parseBeginOfDay(filter.getStartDate()),
                DateTimeUtil.parseEndOfDay(filter.getEndDate()),
                filter.getOriginSource()
        );
        return Result.success(pagedNews);
    }

    /**
     * 按标题模糊搜索新闻并按过滤器过滤，以分页方式返回
     *
     * @param current  当前页码
     * @param pageSize 页大小
     * @param query    搜索关键词
     * @param filter   筛选条件
     * @return 新闻条目VO列表
     */
    @PostMapping("/news/search")
    public Result<PagedNews> searchNewsByTitle(
            @RequestParam int current,
            @RequestParam int pageSize,
            @RequestParam String query,
            @RequestBody(required = false) Filter filter) {
        PagedNews pagedNews = newsService.searchNewsByTitleFiltered(current, pageSize, query,
                filter.getCategory(),
                DateTimeUtil.parseBeginOfDay(filter.getStartDate()),
                DateTimeUtil.parseEndOfDay(filter.getEndDate()),
                filter.getOriginSource()
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

    /**
     * 获取前N个最可能的新闻分类
     *
     * @param text 新闻标题字符串
     * @param topN topN个数
     * @return 分类结果
     */
    @GetMapping("/news/top-n-classify")
    public Result<List<ClassifyResultVO>> getTopNClassify(@RequestParam String text, @RequestParam int topN) {
        List<ClassifyResultVO> result = newsService.topNClassify(text, topN);
        return Result.success(result);
    }

    /**
     * 词云统计
     *
     * @param id 新闻id
     */
    @GetMapping("/news/word")
    public Result<NewsSegmentVO> getWordCloud(@RequestParam Long id) {
        NewsSegmentVO newsSegmentVO = newsService.getNewsWordCloud(id);
        return Result.success(newsSegmentVO);
    }

    /**
     * 获取今日词云
     *
     * @param count 词云数量
     * @return 词云列表
     */
    @GetMapping("/news/wordCloudToday")
    public Result<List<NewsWordVO>> getWordCloudToday(@RequestParam Integer count) {
        List<NewsWordVO> resultList = newsService.getNewsWordCloudToday(count);
        return Result.success(resultList);
    }
}
