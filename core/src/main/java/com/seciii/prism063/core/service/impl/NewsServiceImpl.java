package com.seciii.prism063.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seciii.prism063.common.enums.ErrorType;
import com.seciii.prism063.common.exception.NewsException;
import com.seciii.prism063.core.mapper.NewsMapper;
import com.seciii.prism063.core.pojo.po.NewsPO;
import com.seciii.prism063.core.pojo.vo.news.NewsItemVO;
import com.seciii.prism063.core.pojo.vo.news.NewsVO;
import com.seciii.prism063.core.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 新闻服务接口实现类
 *
 * @author xueruichen
 * @date 2024.02.29
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper,NewsPO> implements NewsService {
    @Autowired
    NewsMapper newsMapper;

    @Override
    public List<NewsItemVO> getNewsList() throws NewsException {
        List<NewsPO> newsList=newsMapper.selectList(null);
        if(newsList==null||newsList.isEmpty()){
            throw new NewsException(ErrorType.NEWS_LIST_EMPTY);
        }
        return toNewsVO(newsList);
    }

    @Override
    public List<NewsItemVO> getNewsListByPage(Integer pageNo,Integer pageSize)throws NewsException {
        Page<NewsPO> page= newsMapper.selectPage(new Page<>(pageNo,pageSize),null);
        //TODO: 分页查询异常处理
        return toNewsVO(page.getRecords());
    }

    @Override
    public NewsVO getNewsDetail(Long id)throws NewsException {
        NewsPO newsPO=newsMapper.selectById(id);
        if(newsPO==null){
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        return toNewsVO(newsPO);
    }
    @Override
    public boolean deleteNews(Long id)throws NewsException {
        int result=newsMapper.deleteById(id);
        if(result==0){
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        return true;
    }

    /**
     * 将新闻PO列表转换为新闻条目VO列表
     * @param newsPOList 新闻PO列表
     * @return 新闻条目VO列表
     */
    private List<NewsItemVO> toNewsVO(List<NewsPO> newsPOList) {
        return newsPOList.stream().map(
                newsPO -> NewsItemVO.builder()
                       .id(newsPO.getId())
                       .title(newsPO.getTitle())
                       .source(newsPO.getSource())
                       .time(newsPO.getTime())
                       .build()
        ).toList();
    }

    /**
     * 将新闻PO转换为新闻VO
     * @param newsPO 新闻PO
     * @return 新闻VO
     */
    private NewsVO toNewsVO(NewsPO newsPO){
        return NewsVO.builder()
                .id(newsPO.getId())
                .title(newsPO.getTitle())
                .content(newsPO.getContent())
                .source(newsPO.getSource())
                .time(newsPO.getTime())
                .createTime(newsPO.getCreateTime())
                .updateTime(newsPO.getUpdateTime())
                .build();
    }
}
