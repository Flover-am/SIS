package com.seciii.prism063.core.service;

import com.seciii.prism063.common.exception.NewsException;
import com.seciii.prism063.core.pojo.vo.news.NewsItemVO;
import com.seciii.prism063.core.pojo.vo.news.NewsVO;

import java.util.List;

/**
 * 新闻服务接口类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
public interface NewsService {
    /**
     * 获取新闻列表
     * @return 新闻条目VO列表
     */
    List<NewsItemVO> getNewsList();

    /**
     * 按页数和页大小获取新闻列表
     * @param pageNo 页数
     * @return 对应页数新闻条目VO列表
     */
    List<NewsItemVO> getNewsListByPage(Integer pageNo,Integer pageSize);

    /**
     * 获取新闻详情
     * @param id 新闻id
     * @return 新闻VO
     */
    NewsVO getNewsDetail(Long id);
    /**
     * 删除新闻
     * @param id 新闻id
     * @return 删除结果，成功为true
     */
    boolean deleteNews(Long id);

}
