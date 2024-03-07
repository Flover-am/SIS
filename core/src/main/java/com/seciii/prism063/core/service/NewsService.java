package com.seciii.prism063.core.service;

import com.seciii.prism063.common.exception.NewsException;
import com.seciii.prism063.core.pojo.vo.news.NewNews;
import com.seciii.prism063.core.pojo.vo.news.NewsItemVO;
import com.seciii.prism063.core.pojo.vo.news.NewsVO;

import java.time.LocalDate;
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
     * 修改新闻标题
     * @param id 新闻id
     * @param title 新标题
     */
    void modifyNewsTitle(Long id, String title);

    /**
     * 修改新闻内容
     * @param id 新闻id
     * @param content 新内容
     */
    void modifyNewsContent(Long id, String content);

    /**
     * 修改新闻来源
     * @param id 新闻id
     * @param source 新来源
     */
    void modifyNewsSource(Long id, String source);

    /**
     * 删除新闻
     * @param id 新闻id
     */
    void deleteNews(Long id);

    /**
     * 新增新闻
     * @param newNews 新增的新闻对象
     */
    void addNews(NewNews newNews);

    /**
     * 分页获取过滤后的新闻列表
     * @param pageNo 页数
     * @param pageSize 页大小
     * @param category 新闻分类
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 过滤后的新闻列表
     */
    List<NewsItemVO> filterNewsPaged(int pageNo, int pageSize, List<String> category, LocalDate startTime,LocalDate endTime);

    /**
     * 按标题模糊搜索新闻
     * @param title 新闻标题
     * @return 搜索结果新闻列表
     */
    List<NewsItemVO> searchNewsByTitle(String title);
}
