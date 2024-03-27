package com.seciii.prism030.core.service;

import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.pojo.vo.news.NewsVO;

public interface ESNewsService {

    void addESNews(NewNews newNews,long newsId);

    void deleteESNewsByNewsId(Long id);

    void modifyESNewsTitle(Long id, String title);

    void modifyESNewsContent(Long id, String content);

    void modifyESNewsSource(Long id, String source);

    NewsVO getESNewsDetailByNewsId(Long id);
}
