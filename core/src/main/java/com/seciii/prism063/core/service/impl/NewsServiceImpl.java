package com.seciii.prism063.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seciii.prism063.common.exception.error.ErrorType;
import com.seciii.prism063.core.enums.CategoryType;
import com.seciii.prism063.common.exception.NewsException;
import com.seciii.prism063.core.mapper.news.NewsMapper;
import com.seciii.prism063.core.pojo.dto.PagedNews;
import com.seciii.prism063.core.pojo.po.news.NewsPO;
import com.seciii.prism063.core.pojo.vo.news.NewNews;
import com.seciii.prism063.core.pojo.vo.news.NewsItemVO;
import com.seciii.prism063.core.pojo.vo.news.NewsVO;
import com.seciii.prism063.core.service.NewsService;
import com.seciii.prism063.core.utils.DateTimeUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 新闻服务接口实现类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Service
public class NewsServiceImpl implements NewsService {

    private final NewsMapper newsMapper;

    public NewsServiceImpl(NewsMapper newsMapper) {
        this.newsMapper = newsMapper;
    }

    @Override
    @Deprecated
    public PagedNews getNewsList() throws NewsException {
        QueryWrapper<NewsPO> newsQueryWrapper = new QueryWrapper<>();
        newsQueryWrapper.select("*");
        List<NewsPO> newsList = newsMapper.selectList(newsQueryWrapper);
        return new PagedNews(newsMapper.selectCount(newsQueryWrapper), toNewsVO(newsList));
    }

    @Override
    public void addNews(NewNews newNews) {
        newsMapper.insert(toNewsPO(newNews));
    }

    @Override
    @Deprecated
    public PagedNews getNewsListByPage(Integer pageNo, Integer pageSize) throws NewsException {
        long count = newsMapper.getFilteredNewsCount(null, null, null, null);
        List<NewsPO> newsList = newsMapper.getFilteredNewsByPage(pageNo, pageSize, null, null, null, null);
        return new PagedNews(count, toNewsVO(newsList));
    }

    @Override
    public NewsVO getNewsDetail(Long id) throws NewsException {
        NewsPO newsPO = newsMapper.selectById(id);
        if (newsPO == null) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        return toNewsVO(newsPO);
    }

    @Override
    public void modifyNewsTitle(Long id, String title) throws NewsException {
        NewsPO newsPO = newsMapper.selectById(id);
        if (newsPO == null) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        if (!newsPO.getTitle().equals(title)) {
            newsPO.setTitle(title);
            newsMapper.updateById(newsPO);
        }
    }

    @Override
    public void modifyNewsContent(Long id, String content) throws NewsException {
        NewsPO newsPO = newsMapper.selectById(id);
        if (newsPO == null) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        if (!newsPO.getContent().equals(content)) {
            newsPO.setContent(content);
            newsMapper.updateById(newsPO);
        }
    }

    @Override
    public void modifyNewsSource(Long id, String source) throws NewsException {
        NewsPO newsPO = newsMapper.selectById(id);
        if (newsPO == null) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        if (!newsPO.getOriginSource().equals(source)) {
            newsPO.setOriginSource(source);
            newsMapper.updateById(newsPO);
        }
    }

    @Override
    public void deleteNews(Long id) throws NewsException {
        int result = newsMapper.deleteById(id);
        if (result == 0) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
    }

    @Override
    public void deleteMultipleNews(List<Long> idList) {
        newsMapper.deleteBatchIds(idList);
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
        long count = newsMapper.getFilteredNewsCount(categoryIdList, startTime, endTime, originSource);
        long offset = (long) (pageNo - 1) * pageSize;
        if (offset >= count) {
            throw new NewsException(ErrorType.NEWS_PAGE_OVERFLOW);
        }
        List<NewsPO> result = newsMapper.getFilteredNewsByPage(pageSize, (int) offset, categoryIdList, startTime, endTime, originSource);
        return new PagedNews(count, toNewsVO(result));
    }

    @Override
    @Deprecated
    public PagedNews searchNewsByTitle(String title) {
        long count = newsMapper.getSearchedFilteredNewsCount(title, null, null, null, null);
        List<NewsPO> result = newsMapper.searchFilteredNewsByPage(0, 0, title, null, null, null, null);
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
        long count = newsMapper.getSearchedFilteredNewsCount(title, categoryIdList, startTime, endTime, originSource);
        long offset = (long) (pageNo - 1) * pageSize;
        if (offset >= count) {
            throw new NewsException(ErrorType.NEWS_PAGE_OVERFLOW);
        }
        List<NewsPO> result = newsMapper.searchFilteredNewsByPage(pageSize, (int) offset, title, categoryIdList, startTime, endTime, originSource);
        return new PagedNews(count, toNewsVO(result));
    }

    /**
     * 将字符串拆分为单字
     *
     * @param s 待拆分的字符串
     * @return 拆分后的字符串
     */
    private String splitString(String s) {
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

    /**
     * 获取过滤查询条件
     *
     * @param category  分类数组
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 查询条件QueryWrapper对象
     */
    @Deprecated
    private QueryWrapper<NewsPO> getFilterQueryWrapper(List<String> category,
                                                       LocalDateTime startTime,
                                                       LocalDateTime endTime) {
        QueryWrapper<NewsPO> filterQueryWrapper = new QueryWrapper<>();
        filterQueryWrapper = filterQueryWrapper.select("*");
        if (category != null && !category.isEmpty()) {
            List<Integer> categoryTypeList = category.stream().map(x -> CategoryType.getCategoryType(x).toInt()).toList();
            filterQueryWrapper = filterQueryWrapper.in("category", categoryTypeList);
        }
        if (startTime != null && endTime != null) {
            filterQueryWrapper = filterQueryWrapper.between("source_time", startTime, endTime);
        } else if (startTime != null) {
            filterQueryWrapper = filterQueryWrapper.ge("source_time", startTime);
        } else if (endTime != null) {
            filterQueryWrapper = filterQueryWrapper.le("source_time", endTime);
        }
        return filterQueryWrapper;
    }
}
