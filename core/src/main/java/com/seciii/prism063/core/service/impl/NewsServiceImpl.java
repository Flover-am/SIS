package com.seciii.prism063.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seciii.prism063.common.exception.error.ErrorType;
import com.seciii.prism063.core.enums.CategoryType;
import com.seciii.prism063.common.exception.NewsException;
import com.seciii.prism063.core.mapper.NewsMapper;
import com.seciii.prism063.core.pojo.dto.PagedNews;
import com.seciii.prism063.core.pojo.po.NewsPO;
import com.seciii.prism063.core.pojo.vo.news.NewNews;
import com.seciii.prism063.core.pojo.vo.news.NewsItemVO;
import com.seciii.prism063.core.pojo.vo.news.NewsVO;
import com.seciii.prism063.core.service.NewsService;
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
public class NewsServiceImpl extends ServiceImpl<NewsMapper, NewsPO> implements NewsService {

    private final NewsMapper newsMapper;

    public NewsServiceImpl(NewsMapper newsMapper) {
        this.newsMapper = newsMapper;
    }

    @Override
    public PagedNews getNewsList() throws NewsException {
        QueryWrapper<NewsPO> newsQueryWrapper = new QueryWrapper<>();
        newsQueryWrapper.select("*");
        List<NewsPO> newsList = newsMapper.selectList(newsQueryWrapper);
        return new PagedNews(newsMapper.selectCount(newsQueryWrapper),toNewsVO(newsList));
    }

    @Override
    public void addNews(NewNews newNews) {
        newsMapper.insert(toNewsPO(newNews));
    }

    @Override
    public PagedNews getNewsListByPage(Integer pageNo, Integer pageSize) throws NewsException {
        QueryWrapper<NewsPO> newsQueryWrapper = new QueryWrapper<NewsPO>().select("*");
        Page<NewsPO> page = newsMapper.selectPage(new Page<>(pageNo, pageSize), newsQueryWrapper);
        return new PagedNews(newsMapper.selectCount(newsQueryWrapper),toNewsVO(page.getRecords()));
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
        newsPO.setTitle(title);
        newsMapper.updateById(newsPO);
    }

    @Override
    public void modifyNewsContent(Long id, String content) throws NewsException {
        NewsPO newsPO = newsMapper.selectById(id);
        if (newsPO == null) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        newsPO.setContent(content);
        newsMapper.updateById(newsPO);
    }

    @Override
    public void modifyNewsSource(Long id, String source) throws NewsException {
        NewsPO newsPO = newsMapper.selectById(id);
        if (newsPO == null) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        newsPO.setOriginSource(source);
        newsMapper.updateById(newsPO);
    }

    @Override
    public void deleteNews(Long id) throws NewsException {
        int result = newsMapper.deleteById(id);
        if (result == -1) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
    }

    @Override
    public PagedNews filterNewsPaged(
            int pageNo,
            int pageSize,
            List<String> category,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        QueryWrapper<NewsPO> filterQueryWrapper = getFilterQueryWrapper(category, startTime, endTime);

        Page<NewsPO> page = newsMapper.selectPage(new Page<>(pageNo, pageSize), filterQueryWrapper);
        return new PagedNews(newsMapper.selectCount(filterQueryWrapper),toNewsVO(page.getRecords()));
    }

    @Override
    public PagedNews searchNewsByTitle(String title) {
        QueryWrapper<NewsPO> searchQueryWrapper = new QueryWrapper<>();
        searchQueryWrapper.select("*");
        searchQueryWrapper.like("title", title);
        List<NewsPO> newsList = newsMapper.selectList(searchQueryWrapper);
        return new PagedNews(newsMapper.selectCount(searchQueryWrapper),toNewsVO(newsList));
    }

    @Override
    public PagedNews searchNewsByTitleFiltered(
            int pageNo,
            int pageSize,
            String title,
            List<String> category,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        QueryWrapper<NewsPO> filterSearchWrapper = getFilterQueryWrapper(category, startTime, endTime);
        filterSearchWrapper.select("*");
        filterSearchWrapper.like("title", title);

        Page<NewsPO> page = newsMapper.selectPage(new Page<>(pageNo, pageSize), filterSearchWrapper);
        return new PagedNews(newsMapper.selectCount(filterSearchWrapper),toNewsVO(page.getRecords()));
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
                        .sourceTime(newsPO.getSourceTime())
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
                .sourceTime(newsPO.getSourceTime())
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
                .sourceTime(newNews.getSourceTime())
                .link(newNews.getLink())
                .sourceLink(newNews.getSourceLink())
                .category(CategoryType.getCategoryType(newNews.getCategory()).toInt())
                .build();
    }

    /**
     * 获取过滤查询条件
     * @param category 分类数组
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 查询条件QueryWrapper对象
     */
    private QueryWrapper<NewsPO> getFilterQueryWrapper(List<String> category,
                                                       LocalDateTime startTime,
                                                       LocalDateTime endTime) {
        QueryWrapper<NewsPO> filterQueryWrapper = new QueryWrapper<>();
        filterQueryWrapper.select("*");
        if (category != null && !category.isEmpty()) {
            List<Integer> categoryTypeList = category.stream().map(x -> CategoryType.getCategoryType(x).toInt()).toList();
            filterQueryWrapper.in("category", categoryTypeList);
        }
        if (startTime != null && endTime != null) {
            filterQueryWrapper.between("source_time", startTime, endTime);
        } else if (startTime != null) {
            filterQueryWrapper.ge("source_time", startTime);
        } else if (endTime != null) {
            filterQueryWrapper.le("source_time", endTime);
        }
        return filterQueryWrapper;
    }
}
