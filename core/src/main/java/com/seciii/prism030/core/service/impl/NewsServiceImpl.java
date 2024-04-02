package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.common.exception.error.ErrorType;
import com.seciii.prism030.core.enums.CategoryType;
import com.seciii.prism030.common.exception.NewsException;
import com.seciii.prism030.core.mapper.news.NewsMapperMP;
import com.seciii.prism030.core.pojo.dto.PagedNews;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.pojo.vo.news.NewsItemVO;
import com.seciii.prism030.core.pojo.vo.news.NewsVO;
import com.seciii.prism030.core.service.NewsService;
import com.seciii.prism030.core.utils.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 新闻服务接口实现类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
//@Service
@Deprecated
public class NewsServiceImpl implements NewsService {

    private final NewsMapperMP newsMapperMP;

    public NewsServiceImpl(NewsMapperMP newsMapperMP) {
        this.newsMapperMP = newsMapperMP;
    }

    @Override
    public void addNews(NewNews newNews) {
        newsMapperMP.insert(toNewsPO(newNews));
    }

    @Override
    public NewsVO getNewsDetail(Long id) throws NewsException {
        NewsPO newsPO = newsMapperMP.selectById(id);
        if (newsPO == null) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        return toNewsVO(newsPO);
    }

    @Override
    public void modifyNewsTitle(Long id, String title) throws NewsException {
        NewsPO newsPO = newsMapperMP.selectById(id);
        if (newsPO == null) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        if (!newsPO.getTitle().equals(title)) {
            newsPO.setTitle(title);
            newsMapperMP.updateById(newsPO);
        }
    }

    @Override
    public void modifyNewsContent(Long id, String content) throws NewsException {
        NewsPO newsPO = newsMapperMP.selectById(id);
        if (newsPO == null) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        if (!newsPO.getContent().equals(content)) {
            newsPO.setContent(content);
            newsMapperMP.updateById(newsPO);
        }
    }

    @Override
    public void modifyNewsSource(Long id, String source) throws NewsException {
        NewsPO newsPO = newsMapperMP.selectById(id);
        if (newsPO == null) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        if (!newsPO.getOriginSource().equals(source)) {
            newsPO.setOriginSource(source);
            newsMapperMP.updateById(newsPO);
        }
    }

    @Override
    public void deleteNews(Long id) throws NewsException {
        int result = newsMapperMP.deleteById(id);
        if (result == 0) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
    }

    @Override
    public void deleteMultipleNews(List<Long> idList) {
        newsMapperMP.deleteBatchIds(idList);
    }

    @Override
    public PagedNews filterNewsPaged(
            int pageNo,
            int pageSize,
            List<String> category,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String originSource
    ) {
        originSource = splitString(originSource);
        List<Integer> categoryIdList = getCategoryTypeList(category);
        long count = newsMapperMP.getFilteredNewsCount(categoryIdList, startTime, endTime, originSource);
        if (count == 0) {
            return new PagedNews(0, Collections.emptyList());
        }
        long offset = (long) (pageNo - 1) * pageSize;

        if (offset >= count) {
            throw new NewsException(ErrorType.NEWS_PAGE_OVERFLOW, "新闻页获取错误");
        }
        List<NewsPO> result = newsMapperMP.getFilteredNewsByPage(pageSize, (int) offset, categoryIdList, startTime, endTime, originSource);
        return new PagedNews(count, toNewsVO(result));
    }

    @Override
    public PagedNews searchNewsByTitleFiltered(
            int pageNo,
            int pageSize,
            String title,
            List<String> category,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String originSource
    ) {
        originSource = splitString(originSource);
        title = splitString(title);
        List<Integer> categoryIdList = getCategoryTypeList(category);
        long count = newsMapperMP.getSearchedFilteredNewsCount(title, categoryIdList, startTime, endTime, originSource);
        if (count == 0) {
            return new PagedNews(0, Collections.emptyList());
        }
        long offset = (long) (pageNo - 1) * pageSize;
        if (offset >= count) {
            throw new NewsException(ErrorType.NEWS_PAGE_OVERFLOW, "新闻页获取错误");
        }
        List<NewsPO> result = newsMapperMP.searchFilteredNewsByPage(pageSize, (int) offset, title, categoryIdList, startTime, endTime, originSource);
        return new PagedNews(count, toNewsVO(result));
    }

    /**
     * 将字符串拆分为单字
     *
     * @param s 待拆分的字符串
     * @return 拆分后的字符串
     */
    private String splitString(String s) {
        if (s == null) {
            return null;
        }
        String[] split = s.split("");
        return String.join("%", split);
    }

    /**
     * 将字符串类别列表转换为其对应下标列表
     *
     * @param category 字符串类别列表
     * @return 下标列表
     */
    private List<Integer> getCategoryTypeList(List<String> category) {
        if (category == null || category.isEmpty()) {
            return null;
        }
        return category.stream().map(
                x -> CategoryType.getCategoryType(x).toInt()
        ).toList();
    }

    /**
     * 将新闻PO列表转换为新闻条目VO列表
     *
     * @param newsPOList 新闻PO列表
     * @return 新闻条目VO列表
     */
    private List<NewsItemVO> toNewsVO(List<NewsPO> newsPOList) {
        return newsPOList.stream().map(
                newsPO -> NewsItemVO.builder()
                        .id(newsPO.getId())
                        .title(newsPO.getTitle())
                        .originSource(newsPO.getOriginSource())
                        .sourceTime(DateTimeUtil.defaultFormat(newsPO.getSourceTime()))
                        .category(CategoryType.getCategoryType(newsPO.getCategory()).getCategoryEN())
                        .link(newsPO.getLink())
                        .updateTime(DateTimeUtil.defaultFormat(newsPO.getUpdateTime()))
                        .build()
        ).toList();
    }

    /**
     * 将新闻PO转换为新闻VO
     *
     * @param newsPO 新闻PO
     * @return 新闻VO
     */
    private NewsVO toNewsVO(NewsPO newsPO) {
        return NewsVO.builder()
                .id(newsPO.getId())
                .title(newsPO.getTitle())
                .content(newsPO.getContent())
                .originSource(newsPO.getOriginSource())
                .sourceTime(DateTimeUtil.defaultFormat(newsPO.getSourceTime()))
                .link(newsPO.getLink())
                .sourceLink(newsPO.getSourceLink())
                .category(CategoryType.getCategoryType(newsPO.getCategory()).getCategoryEN())
                .createTime(newsPO.getCreateTime())
                .updateTime(newsPO.getUpdateTime())
                .build();
    }

    /**
     * 将新闻NewNews对象转换为新闻PO
     *
     * @param newNews 新闻NewNews对象
     * @return 新闻PO
     */
    private NewsPO toNewsPO(NewNews newNews) {
        return NewsPO.builder()
                .title(newNews.getTitle())
                .content(newNews.getContent())
                .originSource(newNews.getOriginSource())
                .sourceTime(DateTimeUtil.defaultParse(newNews.getSourceTime()))
                .link(newNews.getLink())
                .sourceLink(newNews.getSourceLink())
                .category(CategoryType.getCategoryType(newNews.getCategory()).toInt())
                .build();
    }
}
