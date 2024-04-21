package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.common.exception.NewsException;
import com.seciii.prism030.common.exception.error.ErrorType;
import com.seciii.prism030.core.aspect.annotation.Add;
import com.seciii.prism030.core.aspect.annotation.Modified;
import com.seciii.prism030.core.dao.news.NewsDAOMongo;
import com.seciii.prism030.core.decorator.classifier.Classifier;
import com.seciii.prism030.core.decorator.segment.TextSegment;
import com.seciii.prism030.core.enums.CategoryType;
import com.seciii.prism030.core.pojo.dto.NewsWordDetail;
import com.seciii.prism030.core.pojo.dto.PagedNews;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.po.news.NewsSegmentPO;
import com.seciii.prism030.core.pojo.po.news.NewsWordPO;
import com.seciii.prism030.core.pojo.vo.news.*;
import com.seciii.prism030.core.service.NewsService;
import com.seciii.prism030.core.service.SummaryService;
import com.seciii.prism030.core.utils.NewsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

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
@EnableAspectJAutoProxy
public class NewsServiceMongoImpl implements NewsService {
    private NewsDAOMongo newsDAOMongo;
    private Classifier classifier;
    private TextSegment textSegment;

    private SummaryService summaryService;

    @Autowired
    public void setRedisService(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @Autowired
    public void setNewsDAOMongo(NewsDAOMongo newsDAOMongo) {
        this.newsDAOMongo = newsDAOMongo;
    }

    @Autowired
    public void setClassifier(Classifier classifier) {
        this.classifier = classifier;
    }

    @Autowired
    public void setTextSegment(TextSegment textSegment) {
        this.textSegment = textSegment;
    }

    @Override
    public String getLastModified() {
        return summaryService.getLastModified();
    }

    /**
     * 今日新闻数量
     *
     * @return 新闻数量
     */
    @Override
    public Integer countDateNews() {
        return summaryService.countDateNews();
    }

    /**
     * 今日新闻分类数量
     *
     * @param category 新闻分类
     * @return 新闻数量
     */
    @Override
    public Integer countCategoryNews(int category) {
        return summaryService.countCategoryNews(category);
    }

    /**
     * 今日所有种类新闻数量
     *
     * @return 每个种类的新闻数量
     */
    @Override
    public List<NewsCategoryCountVO> countAllCategoryNews() {
        List<NewsCategoryCountVO> newsCategoryCountVOList = new ArrayList<>();
        for (int i = 0; i < CategoryType.values().length; i++) {
            if (CategoryType.of(i) != CategoryType.OTHER){
                newsCategoryCountVOList.add(NewsCategoryCountVO.builder().category(CategoryType.of(i).toString()).count(summaryService.countCategoryNews(i)).build());
            }
        }
        return newsCategoryCountVOList;
    }

    /**
     * 一段时间内新闻数量
     *
     * @return 每天每种新闻数量
     */
    @Override
    public List<NewsDateCountVO> countPeriodNews(String startTime, String endTime) {
        LocalDate startDate = LocalDate.parse(startTime);
        LocalDate endDate = LocalDate.parse(endTime);
        List<NewsDateCountVO> newsDateCountVoList = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {
            List<NewsCategoryCountVO> newsCategoryCountVOList = new ArrayList<>();
            for (int i = 0; i < CategoryType.values().length; i++) {
                if (CategoryType.of(i) != CategoryType.OTHER){
                    newsCategoryCountVOList.add(NewsCategoryCountVO.builder().category(CategoryType.of(i).toString()).count(summaryService.countCategoryNews(i, date)).build());
                }
            }
            newsDateCountVoList.add(NewsDateCountVO.builder().date(date.toString()).newsCategoryCounts(newsCategoryCountVOList).build());
        }
        return newsDateCountVoList;
    }


    @Override
    public Integer countWeekNews() {
        return summaryService.countWeekNews();
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
    @Modified
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
    @Modified
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
    @Modified
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
    @Modified
    public void deleteNews(Long id) {
        int result = newsDAOMongo.deleteById(id);
        if (result == -1) {
            log.error(String.format("News with id %d not found", id));
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
    }

    /**
     * 批量删除新闻
     *
     * @param idList 新闻id列表
     */
    @Override
    @Modified
    public void deleteMultipleNews(List<Long> idList) {
        newsDAOMongo.batchDeleteNews(idList);
    }


    /**
     * 新增新闻
     *
     * @param newNews 新增的新闻对象
     */
    @Override
    @Modified
    @Add
    public long addNews(NewNews newNews) {
        return newsDAOMongo.insert(NewsUtil.toNewsPO(newNews));
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

    /**
     * 获取新闻词云
     *
     * @param id 新闻id
     * @return 词云结果
     */
    @Override
    public NewsSegmentVO getNewsWordCloud(long id) {

        //检查该id的新闻是否存在
        if (newsDAOMongo.getNewsById(id) == null) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }

        //检查所查词云是否已持久化
        NewsSegmentPO newsSegmentPO = newsDAOMongo.getNewsSegmentById(id);
        if (newsSegmentPO != null) {
            return NewsUtil.toNewsSegmentVO(newsSegmentPO);
        }

        //调用分词服务并持久化
        try {
            NewsSegmentPO newNewsSegmentPO = generateAndSaveWordCloud(id, newsDAOMongo.getNewsById(id).getContent());
            return NewsUtil.toNewsSegmentVO(newNewsSegmentPO);
        } catch (ResourceAccessException e) {
            log.error(e.getMessage());
            throw new NewsException(ErrorType.NEWS_SEGMENT_SERVICE_UNAVAILABLE);
        }

    }

    /**
     * 生成并保存新闻词云
     *
     * @param id   新闻id
     * @param text 新闻内容
     * @return 词云结果
     */
    @Override
    public NewsSegmentPO generateAndSaveWordCloud(long id, String text) {
        if (text.contains("责任编辑")) {
            text = text.substring(0, text.indexOf("责任编辑"));
        }
        NewsWordDetail[] newsWordDetails = textSegment.rank(text);
        List<NewsWordDetail> filteredNewsWordDetail = NewsUtil.filterNewsWordDetail(newsWordDetails);
        List<NewsWordPO> newsWordPOList = new ArrayList<>();
        for (NewsWordDetail newsWordDetail : filteredNewsWordDetail) {
            boolean isContained = false;
            for (NewsWordPO newsWordPO : newsWordPOList) {
                if (newsWordPO.getText().equals(newsWordDetail.getText())) {
                    newsWordPO.setCount(newsWordPO.getCount() + 1);
                    isContained = true;
                    break;
                }
            }
            if (!isContained) {
                newsWordPOList.add(NewsWordPO.builder()
                        .text(newsWordDetail.getText())
                        .count(1)
                        .build());
            }
        }
        NewsSegmentPO newNewsSegmentPO = NewsSegmentPO.builder()
                .id(id)
                .content(newsWordPOList.toArray(new NewsWordPO[0]))
                .build();

        //插入新生成的词云到数据库
        int code = newsDAOMongo.insertSegment(newNewsSegmentPO);
        if (code != 0) {
            // 插入到数据库失败，但已得到分词结果
            log.error(String.format("Failed to insert news segment with id %d. ", id));
        }
        return newNewsSegmentPO;
    }

    /**
     * 获取今日新闻词云
     *
     * @param count 词语数量
     * @return 词云结果
     */
    @Override
    public List<NewsWordVO> getNewsWordCloudToday(int count) {
        List<NewsWordPO> resultList = null;
        List<NewsWordPO> redisResultList = null;
        try {
            redisResultList = summaryService.getTopNWordCloudToday(count);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        boolean isRedisAvailable = redisResultList != null && !redisResultList.isEmpty();
        if (isRedisAvailable) {
            resultList = redisResultList;
        } else {
            resultList = newsDAOMongo.getTopNWordCloudToday(count);
        }
        if (resultList == null){
            throw new NewsException(ErrorType.NEWS_SEGMENT_SERVICE_UNAVAILABLE);
        }
        if (!isRedisAvailable) {
            updateWordCloudToday();
        }
        return resultList.stream().map(
                x -> NewsWordVO.builder()
                        .text(x.getText())
                        .count(x.getCount())
                        .build()
        ).toList();
    }

    /**
     * 更新当日词云到Redis中
     */
    @Override
    public void updateWordCloudToday() {
        List<NewsWordPO> wordCloud = newsDAOMongo.getWordCloudToday();
        summaryService.updateWordCloudToday(wordCloud);
    }

    @Override
    public Integer diffTodayAndYesterday() {
return summaryService.diffTodayAndYesterday();
    }

}
