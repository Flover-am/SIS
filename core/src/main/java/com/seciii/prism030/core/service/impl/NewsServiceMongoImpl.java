package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.common.exception.NewsException;
import com.seciii.prism030.common.exception.error.ErrorType;
import com.seciii.prism030.core.classifier.Classifier;
import com.seciii.prism030.core.dao.news.NewsDAOMongo;
import com.seciii.prism030.core.enums.CategoryType;
import com.seciii.prism030.core.pojo.dto.PagedNews;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.vo.news.*;
import com.seciii.prism030.core.service.NewsService;
import com.seciii.prism030.core.utils.NewsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 新闻服务类的MongoDB实现
 *
 * @author wang mingsong
 * @date 2024.03.23
 */
@Slf4j
@Service
public class NewsServiceMongoImpl implements NewsService {
    private NewsDAOMongo newsDAOMongo;
    private Classifier classifier;

    private RedisService redisService;

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Autowired
    public void setNewsDAOMongo(NewsDAOMongo newsDAOMongo) {
        this.newsDAOMongo = newsDAOMongo;
    }

    @Autowired
    public void setClassifier(Classifier classifier) {
        this.classifier = classifier;
    }


    /**
     *
     * 今日新闻数量
     * @return 新闻数量
     */

    @Override
    public Integer countDateNews() {
        return redisService.countDateNews();
    }

    /**
     * 今日新闻分类数量
     *
     * @param category 新闻分类
     * @return 新闻数量
     */
    @Override
    public Integer countCategoryNews(int category) {
        return redisService.countCategoryNews(category);
    }

    /**
     * 今日所有种类新闻数量
     * @return 每个种类的新闻数量
     */
    @Override
    public List<NewsCategoryCountVO> countAllCategoryNews() {
        List<NewsCategoryCountVO> newsCategoryCountVOList = new ArrayList<>();
        for (int i = 0; i < CategoryType.values().length; i++) {
            newsCategoryCountVOList.add(NewsCategoryCountVO.builder().category(CategoryType.of(i).toString()).count(redisService.countCategoryNews(i)).build());
        }
        return newsCategoryCountVOList;
    }

    /**
     * 一段时间内新闻数量
     * @return 每天每种新闻数量
     */
    @Override
    public List<NewsDateCountVo> countPeriodNews(String startTime, String endTime) {
        LocalDate startDate = LocalDate.parse(startTime);
        LocalDate endDate = LocalDate.parse(endTime);
        List<NewsDateCountVo> newsDateCountVoList = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            List<NewsCategoryCountVO> newsCategoryCountVOList = new ArrayList<>();
            for (int i = 0; i < CategoryType.values().length; i++) {
                newsCategoryCountVOList.add(NewsCategoryCountVO.builder().category(CategoryType.of(i).toString()).count(redisService.countCategoryNews(i, date)).build());
            }
            newsDateCountVoList.add(NewsDateCountVo.builder().date(date.toString()).newsCategoryCounts(newsCategoryCountVOList).build());
        }
        return newsDateCountVoList;
    }


    @Override
    public Integer countWeekNews() {
        return redisService.countWeekNews();
    }

    /**
     * 获取新闻详情
     *
     * @param id 新闻id
     * @return 新闻VO
     */
    @Override
    public NewsVO getNewsDetail(Long id) {
        NewsPO newsPO = newsDAOMongo.getNewsById(id);
        if (newsPO == null) {
            log.error(String.format("News with id %d not found", id));
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        return NewsUtil.toNewsVO(newsPO);
    }

    /**
     * 修改新闻标题
     *
     * @param id    新闻id
     * @param title 新标题
     */
    @Override
    public void modifyNewsTitle(Long id, String title) {
        int result = newsDAOMongo.updateNewsTitle(id, title);
        if (result == -1) {
            log.error(String.format("News with id %d not found", id));
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
    }

    /**
     * 修改新闻内容
     *
     * @param id      新闻id
     * @param content 新内容
     */
    @Override
    public void modifyNewsContent(Long id, String content) {
        int result = newsDAOMongo.updateNewsContent(id, content);
        if (result == -1) {
            log.error(String.format("News with id %d not found", id));
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
    }

    /**
     * 修改新闻来源
     *
     * @param id     新闻id
     * @param source 新来源
     */
    @Override
    public void modifyNewsSource(Long id, String source) {
        int result = newsDAOMongo.updateNewsSource(id, source);
        if (result == -1) {
            log.error(String.format("News with id %d not found", id));
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
    }

    /**
     * 删除新闻
     *
     * @param id 新闻id
     */
    @Override
    public void deleteNews(Long id) {
        int result = newsDAOMongo.deleteById(id);
        if (result == -1) {
            log.error(String.format("News with id %d not found", id));
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        redisService.deleteNews(newsDAOMongo.getNewsById(id).getCategory());
    }

    /**
     * 批量删除新闻
     *
     * @param idList 新闻id列表
     */
    @Override
    public void deleteMultipleNews(List<Long> idList) {
        newsDAOMongo.batchDeleteNews(idList);
        for (Long id : idList) {
            redisService.deleteNews(newsDAOMongo.getNewsById(id).getCategory());
        }
    }

    /**
     * 新增新闻
     *
     * @param newNews 新增的新闻对象
     */
    @Override
    public void addNews(NewNews newNews) {
        NewsPO newsPO = newsDAOMongo.insert(NewsUtil.toNewsPO(newNews));
        redisService.addNews(newsPO.getCategory());
    }


    /**
     * 分页获取过滤后的新闻列表
     *
     * @param pageNo       页数
     * @param pageSize     页大小
     * @param category     新闻分类
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param originSource 新闻来源
     * @return 过滤后的新闻列表
     */
    @Override
    public PagedNews filterNewsPaged(int pageNo, int pageSize, List<String> category, LocalDateTime startTime, LocalDateTime endTime, String originSource) {
        int pageOffset = pageSize * (pageNo - 1);
        Long total = newsDAOMongo.getFilteredNewsCount(
                NewsUtil.getCategoryTypeList(category),
                startTime,
                endTime,
                originSource
        );
        if (total < pageOffset) {
            log.error(String.format("Page overflow: total %d records with PageNo:%d and PageSize: %d (Offset: %d)",
                    total, pageNo, pageSize, pageOffset));
            throw new NewsException(ErrorType.NEWS_PAGE_OVERFLOW);
        }
        List<NewsPO> newsPOList = newsDAOMongo.getFilteredNewsByPage(
                pageSize,
                pageOffset,
                NewsUtil.getCategoryTypeList(category),
                startTime,
                endTime,
                originSource
        );
        return new PagedNews(total, NewsUtil.toNewsVO(newsPOList));
    }

    /**
     * 按标题模糊搜索新闻并过滤,分页返回
     *
     * @param pageNo       页数
     * @param pageSize     页大小
     * @param title        新闻标题
     * @param category     新闻分类
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param originSource 新闻来源
     * @return 搜索结果新闻列表
     */
    @Override
    public PagedNews searchNewsByTitleFiltered(int pageNo, int pageSize, String title, List<String> category, LocalDateTime startTime, LocalDateTime endTime, String originSource) {
        int pageOffset = pageSize * (pageNo - 1);
        Long total = newsDAOMongo.getSearchedFilteredNewsCount(
                title,
                NewsUtil.getCategoryTypeList(category),
                startTime,
                endTime,
                originSource
        );
        if (total < pageOffset) {
            log.error(String.format("Page overflow: total %d records with PageNo:%d and PageSize: %d (Offset: %d)",
                    total, pageNo, pageSize, pageOffset));
            throw new NewsException(ErrorType.NEWS_PAGE_OVERFLOW);
        }
        List<NewsPO> newsPOList = newsDAOMongo.searchFilteredNewsByPage(
                pageSize,
                pageOffset,
                title,
                NewsUtil.getCategoryTypeList(category),
                startTime,
                endTime,
                originSource
        );
        return new PagedNews(total, NewsUtil.toNewsVO(newsPOList));
    }

    /**
     * 获取新闻前N可能的分类结果
     *
     * @param text 新闻标题字符串
     * @param topN topN个数
     * @return topN分类结果
     */
    @Override
    public List<ClassifyResultVO> topNClassify(String text, int topN) {
        return classifier.topNClassify(text, topN).stream().map(
                pair -> new ClassifyResultVO(pair.getFirst().toString(), pair.getSecond())
        ).toList();
    }
}
