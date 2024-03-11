package com.seciii.prism063.core.pojo.dto;

import com.seciii.prism063.core.pojo.vo.news.NewsItemVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 分页获取新闻列表，以及新闻总数
 * @author wang mingsong
 * @date 2024.03.12
 */
@Getter
@AllArgsConstructor
public class PagedNews {
    /**
     * 新闻总数
     */
    private final long totalCount;
    /**
     * 新闻列表
     */
    private final List<NewsItemVO> newsList;

}
