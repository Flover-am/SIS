package com.seciii.prism030.core.service.impl;

import com.aliyun.dashvector.DashVectorClient;
import com.aliyun.dashvector.DashVectorCollection;
import com.aliyun.dashvector.models.Doc;
import com.seciii.prism030.common.exception.NewsException;
import com.seciii.prism030.common.exception.error.ErrorType;
import com.seciii.prism030.core.aspect.annotation.Add;
import com.seciii.prism030.core.aspect.annotation.Modified;
import com.seciii.prism030.core.config.poolconfig.pool.DashVectorClientPool;
import com.seciii.prism030.core.dao.news.NewsDAOMongo;
import com.seciii.prism030.core.decorator.classifier.Classifier;
import com.seciii.prism030.core.decorator.segment.TextSegment;
import com.seciii.prism030.core.enums.CategoryType;
import com.seciii.prism030.core.mapper.news.VectorNewsMapper;
import com.seciii.prism030.core.enums.UpdateType;
import com.seciii.prism030.core.event.publisher.UpdateNewsPublisher;
import com.seciii.prism030.core.pojo.dto.NewsWordDetail;
import com.seciii.prism030.core.pojo.dto.PagedNews;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.po.news.NewsSegmentPO;
import com.seciii.prism030.core.pojo.po.news.NewsWordPO;
import com.seciii.prism030.core.pojo.po.news.VectorNewsPO;
import com.seciii.prism030.core.pojo.vo.news.*;
import com.seciii.prism030.core.service.NewsService;
import com.seciii.prism030.core.service.SummaryService;
import com.seciii.prism030.core.utils.DashVectorUtil;
import com.seciii.prism030.core.utils.NewsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private DashVectorClientPool dashVectorClientPool;
    private VectorNewsMapper vectorNewsMapper;

    @Value("${dashscope.apikey}")
    private String apiKey;

    @Autowired
    public void setVectorNewsMapper(VectorNewsMapper vectorNewsMapper) {
        this.vectorNewsMapper = vectorNewsMapper;
    }

    @Autowired
    public void setDashVectorClientPool(DashVectorClientPool dashVectorClientPool) {
        this.dashVectorClientPool = dashVectorClientPool;
    }
    private UpdateNewsPublisher updateNewsPublisher;

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

    @Autowired
    public void setUpdateNewsPublisher(UpdateNewsPublisher updateNewsPublisher) {
        this.updateNewsPublisher = updateNewsPublisher;
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
    public Integer countTodayNews() {
        return summaryService.countDateNews();
    }

    /**
     * 今日新闻分类数量
     *
     * @param category 新闻分类
     * @return 新闻数量
     */
    @Override
    public Integer countCategoryOfToday(int category) {
        return summaryService.countCategoryNews(category);
    }

    /**
     * 今日所有种类新闻数量
     *
     * @return 每个种类的新闻数量
     */
    @Override
    public List<NewsCategoryCountVO> countAllCategoryOfTodayNews() {
        List<NewsCategoryCountVO> newsCategoryCountVOList = new ArrayList<>();
        for (int i = 0; i < CategoryType.values().length; i++) {
            if (CategoryType.of(i) != CategoryType.OTHER) {
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
            // 用countAllCategoryOfDateNews
            List<Integer> get = summaryService.countAllCategoryOfDateNews(date);
            for (int i = 0; i < get.size(); i++) {
                if (CategoryType.of(i) != CategoryType.OTHER) {
                    newsCategoryCountVOList.add(NewsCategoryCountVO.builder().category(CategoryType.of(i).toString()).count(get.get(i)).build());
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
        return NewsUtil.toNewsItemVO(newsPO);
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
        updateNewsPublisher.publishModifiedNewsEvent(this, newsDAOMongo.getNewsById(id), UpdateType.MODIFY);
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
        updateNewsPublisher.publishModifiedNewsEvent(this, newsDAOMongo.getNewsById(id), UpdateType.MODIFY);
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
        updateNewsPublisher.publishModifiedNewsEvent(this, newsDAOMongo.getNewsById(id), UpdateType.MODIFY);
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
        result = vectorNewsMapper.deleteVectorNewsByNewsId(id);
        if (result == -1) {
            log.error(String.format("News with id %d not found", id));
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        updateNewsPublisher.publishModifiedNewsEvent(this, NewsVO.builder().id(id).build(), UpdateType.DELETE);
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
        NewsPO newsPO = NewsUtil.toNewsPO(newNews);
        long res = newsDAOMongo.insert(newsPO);
        if (res != -1) {
            updateNewsPublisher.publishModifiedNewsEvent(this, newsPO, UpdateType.ADD);
        }
        return res;
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
        return new PagedNews(total, NewsUtil.toNewsItemVO(newsPOList));
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
        return new PagedNews(total, NewsUtil.toNewsItemVO(newsPOList));
    }

    @Override
    public PagedNews searchNewsByVectorFiltered(int pageNo, int pageSize, String query, List<String> category, LocalDateTime startTime, LocalDateTime endTime, String originSource) {
        DashVectorClient client = null;
        PagedNews result;
        try {
            client = dashVectorClientPool.borrowObject();
            DashVectorCollection collection = client.get(DashVectorUtil.COLLECTION_NAME);
            PagedNews filterPagedNews = filterNewsPaged(pageNo, pageSize, category, startTime, endTime, originSource);
            Set<Long> searchedIdSet = new HashSet<>(queryVectorNewsId(query, 10, collection, apiKey));
            List<NewsItemVO> news = new ArrayList<>();
            for (NewsItemVO newsItemVO : filterPagedNews.getNewsList()) {
                if (searchedIdSet.contains(newsItemVO.getId())) {
                    news.add(newsItemVO);
                }
            }
            result = new PagedNews(news.size(), news);
        } catch (Exception e) {
            throw new NewsException(ErrorType.DASHVECTOR_ERROR);
        } finally {
            if (client != null) {
                dashVectorClientPool.returnObject(client);
            }
        }
        return result;
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

        // 字符串合法
        if (text == null || text.isEmpty()) {
            return null;
        }

        // 获取并过滤分词结果
        NewsWordDetail[] newsWordDetails = textSegment.rank(text);
        List<NewsWordDetail> filteredNewsWordDetail = NewsUtil.filterNewsWordDetail(newsWordDetails);

        // 获取词云
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
        try {
            int code = newsDAOMongo.insertSegment(newNewsSegmentPO);
            if (code != 0) {
                // 插入到数据库失败，但已得到分词结果
                log.error(String.format("Failed to insert news segment with id %d. ", id));
            }
        } catch (RuntimeException e) {
            log.error(String.format("Failed to insert news segment with id %d:%s ", id, e.getMessage()));
        }
        return newNewsSegmentPO;
    }

    /**
     * 保存新闻词云
     *
     * @param id                新闻id
     * @param segmentedWordList 分词后的词语列表
     * @return 词云结果
     */
    @Override
    public NewsSegmentPO saveWordCloud(long id, List<String> segmentedWordList) {
        List<NewsWordPO> newsWordPOList = new ArrayList<>();
        for(String word : segmentedWordList){
            boolean isContained = false;
            for (NewsWordPO newsWordPO : newsWordPOList) {
                if (newsWordPO.getText().equals(word)) {
                    newsWordPO.setCount(newsWordPO.getCount() + 1);
                    isContained = true;
                    break;
                }
            }
            if (!isContained) {
                newsWordPOList.add(NewsWordPO.builder()
                        .text(word)
                        .count(1)
                        .build());
            }
        }
        NewsSegmentPO newsSegmentPO=NewsSegmentPO.builder()
                .id(id)
                .content(newsWordPOList.toArray(new NewsWordPO[0]))
                .build();
        try {
            int code = newsDAOMongo.insertSegment(newsSegmentPO);
            if (code != 0) {
                // 插入到数据库失败，但已得到分词结果
                log.error(String.format("Failed to insert news segment with id %d. ", id));
            }
        } catch (RuntimeException e) {
            log.error(String.format("Failed to insert news segment with id %d:%s ", id, e.getMessage()));
        }
        return newsSegmentPO;
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
            if (resultList != null && resultList.isEmpty()) {
                updateWordCloudToday();
                resultList = newsDAOMongo.getTopNWordCloudToday(count);
            }
        }
        if (resultList == null) {
            throw new NewsException(ErrorType.NEWS_SEGMENT_SERVICE_UNAVAILABLE);
        }
        if (!isRedisAvailable) {
            updateRedisWordCloudToday();
        }
        return resultList.stream().map(
                x -> NewsWordVO.builder()
                        .text(x.getText())
                        .count(x.getCount())
                        .build()
        ).toList();
    }

    /**
     * 更新当日词云
     */
    @Override
    public void updateWordCloudToday() {
        // MongoDB更新
        updateMongoWordCloudToday(false);
        // Redis更新
        updateRedisWordCloudToday();
    }

    /**
     * 更新当日新闻词云到MongoDB中
     */
    private void updateMongoWordCloudToday(boolean force) {
        List<Long> todayNewsList = newsDAOMongo.getTodayNewsList();
        for (long id : todayNewsList) {
            NewsSegmentPO newsSegmentPO = newsDAOMongo.getNewsSegmentById(id);
            if (force || newsSegmentPO == null) {
                NewsPO newsPO = newsDAOMongo.getNewsById(id);
                if (newsPO == null) {
                    log.error(String.format("News with id %d not found. ", id));
                    continue;
                }
                if (newsPO.getCategory() == CategoryType.LOTTERY.ordinal() || newsPO.getCategory() == CategoryType.SPORTS.ordinal()) {
                    generateAndSaveWordCloud(id, newsPO.getTitle());
                    continue;
                }
                if (null == generateAndSaveWordCloud(id, newsPO.getContent())) {
                    log.error(String.format("News with id %d has no content. Use title instead. ", id));
                    generateAndSaveWordCloud(id, newsPO.getTitle());
                }

            }
        }
    }

    /**
     * 更新当日新闻词云到Redis中
     */
    private void updateRedisWordCloudToday() {
        List<NewsWordPO> wordCloud = newsDAOMongo.getWordCloudToday();
        summaryService.updateWordCloudToday(wordCloud);
    }


    /**
     * 获取今日相较昨日新闻数量差
     *
     * @return 新闻数量差
     */
    @Override
    public Integer diffTodayAndYesterday() {
        return summaryService.diffTodayAndYesterday();
    }

    @Override
    public List<NewsSourceCountVO> countAllSourceNews() {
        return summaryService.getSourceRank();
    }

    /**
     * 搜索相似向量对应的id
     *
     * @param query 搜索关键词
     * @param topK 需要搜索的条数
     * @param collection 集合
     * @return 搜索到的新闻id
     */
    private List<Long> queryVectorNewsId(String query, int topK, DashVectorCollection collection, String apiKey) {
        List<Doc> docs = DashVectorUtil.queryVectorDoc(query, topK, collection, apiKey);
        List<Long> idList = docs.stream().map(doc -> {
            VectorNewsPO vectorNewsPO = vectorNewsMapper.getVectorNewsByVectorId(doc.getId());
            if (vectorNewsPO == null) {
                throw new NewsException(ErrorType.NEWS_NOT_FOUND);
            }
            return vectorNewsPO.getNewsId();
        }).toList();
        return DashVectorUtil.removeDuplicate(idList);
    }
}
