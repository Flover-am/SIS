package com.seciii.prism063.core.pojo.dto;

import com.seciii.prism063.core.pojo.vo.news.NewsItemVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
public class PagedNews {
    private final long totalCount;
    private final List<NewsItemVO> newsList;

}
