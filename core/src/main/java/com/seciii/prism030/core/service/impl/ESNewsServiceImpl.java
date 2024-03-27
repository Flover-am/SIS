package com.seciii.prism030.core.service.impl;

import com.seciii.prism030.common.exception.NewsException;
import com.seciii.prism030.common.exception.error.ErrorType;
import com.seciii.prism030.core.dao.es.ESNewsDao;
import com.seciii.prism030.core.enums.CategoryType;
import com.seciii.prism030.core.pojo.po.es.ESNewsPO;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.pojo.vo.news.NewsVO;
import com.seciii.prism030.core.service.ESNewsService;
import com.seciii.prism030.core.utils.DateTimeUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ESNewsServiceImpl implements ESNewsService {

    private final ESNewsDao esNewsDao;

    public ESNewsServiceImpl(ESNewsDao esNewsDao ) {
        this.esNewsDao = esNewsDao;
    }

    @Override
    public void addESNews(NewNews newNews,long newsId) {
        esNewsDao.save(toESNewsPO(newNews,newsId));
    }

    @Override
    public void deleteESNewsByNewsId(Long id) {
        ESNewsPO esNewsPO = esNewsDao.findByNewsId(id);
        if (esNewsPO == null) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        esNewsDao.deleteByNewsId(id);
    }

    @Override
    public void modifyESNewsTitle(Long id, String title) {
        ESNewsPO esNewsPO = esNewsDao.findByNewsId(id);;
        if (esNewsPO == null) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        if (!esNewsPO.getTitle().equals(title)) {
            esNewsPO.setTitle(title);
            esNewsDao.save(esNewsPO);
        }
    }

    @Override
    public void modifyESNewsContent(Long id, String content) {
        ESNewsPO esNewsPO = esNewsDao.findByNewsId(id);;
        if (esNewsPO == null) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        if (!esNewsPO.getContent().equals(content)) {
            esNewsPO.setContent(content);
            esNewsDao.save(esNewsPO);
        }
    }

    @Override
    public void modifyESNewsSource(Long id, String source) {
        ESNewsPO esNewsPO = esNewsDao.findByNewsId(id);;
        if (esNewsPO == null) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        if (!esNewsPO.getOriginSource().equals(source)) {
            esNewsPO.setOriginSource(source);
            esNewsDao.save(esNewsPO);
        }
    }

    @Override
    public NewsVO getESNewsDetailByNewsId(Long id) {
        ESNewsPO esNewsPO = esNewsDao.findByNewsId(id);;
        if (esNewsPO == null) {
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        return toNewsVO(esNewsPO);
    }

    @Override
    public List<ESNewsPO> fuzzySearchByTitle(String title) {
        return esNewsDao.findByTitleFuzzy(title);
    }


    /**
     * 将新闻ESPO转换为新闻VO
     *
     * @param esNewsPO 新闻ESPO
     * @return 新闻VO
     */
    private NewsVO toNewsVO(ESNewsPO esNewsPO) {
        return NewsVO.builder()
                .id(esNewsPO.getNewsId())
                .title(esNewsPO.getTitle())
                .content(esNewsPO.getContent())
                .originSource(esNewsPO.getOriginSource())
                .sourceTime(DateTimeUtil.defaultFormat(esNewsPO.getSourceTime()))
                .link(esNewsPO.getLink())
                .sourceLink(esNewsPO.getSourceLink())
                .category(CategoryType.getCategoryType(esNewsPO.getCategory()).getCategoryEN())
                .createTime(esNewsPO.getCreateTime())
                .updateTime(esNewsPO.getUpdateTime())
                .build();
    }

    /**
     * 将新闻NewNews对象转换为新闻ESPO
     *
     * @param newNews 新闻NewNews对象
     * @return 新闻ESPO
     */
    private ESNewsPO toESNewsPO(NewNews newNews, Long newsId) {
        return ESNewsPO.builder()
                .newsId(newsId)
                .title(newNews.getTitle())
                .content(newNews.getContent())
                .originSource(newNews.getOriginSource())
                .sourceTime(DateTimeUtil.defaultParse(newNews.getSourceTime()))
                .link(newNews.getLink())
                .sourceLink(newNews.getSourceLink())
                .category(CategoryType.getCategoryType(newNews.getCategory()).toInt())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }
}
