package com.seciii.prism063.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seciii.prism063.common.enums.CategoryType;
import com.seciii.prism063.common.enums.ErrorType;
import com.seciii.prism063.common.exception.NewsException;
import com.seciii.prism063.core.mapper.NewsMapper;
import com.seciii.prism063.core.pojo.po.NewsPO;
import com.seciii.prism063.core.pojo.vo.news.NewNews;
import com.seciii.prism063.core.pojo.vo.news.NewsItemVO;
import com.seciii.prism063.core.pojo.vo.news.NewsVO;
import com.seciii.prism063.core.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class NewsServiceImpl extends ServiceImpl<NewsMapper,NewsPO> implements NewsService {
    @Autowired
    private NewsMapper newsMapper;


    @Override
    public List<NewsItemVO> getNewsList() throws NewsException {
        QueryWrapper<NewsPO> newsQueryWrapper=new QueryWrapper<>();
        newsQueryWrapper.select("*");
        List<NewsPO> newsList=newsMapper.selectList(newsQueryWrapper);

        return toNewsVO(newsList);
    }
    @Override
    public void addNews(NewNews newNews){
        newsMapper.insert(toNewsPO(newNews));
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
    public void modifyNewsTitle(Long id, String title)throws NewsException {
        NewsPO newsPO=newsMapper.selectById(id);
        if(newsPO==null){
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        newsPO.setTitle(title);
        newsMapper.updateById(newsPO);
    }
    @Override
    public void modifyNewsContent(Long id, String content)throws NewsException {
        NewsPO newsPO=newsMapper.selectById(id);
        if(newsPO==null){
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        newsPO.setContent(content);
        newsMapper.updateById(newsPO);
    }
    @Override
    public void modifyNewsSource(Long id, String source)throws NewsException {
        NewsPO newsPO=newsMapper.selectById(id);
        if(newsPO==null){
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
        newsPO.setOriginSource(source);
        newsMapper.updateById(newsPO);
    }
    @Override
    public void deleteNews(Long id)throws NewsException {
        int result=newsMapper.deleteById(id);
        //TODO: 确定删除接口的返回值
        if(result==0){
            throw new NewsException(ErrorType.NEWS_NOT_FOUND);
        }
    }
    @Override
    public List<NewsItemVO> filterNewsPaged(int pageNo, int pageSize, List<String> category, LocalDateTime startTime, LocalDateTime endTime){
        QueryWrapper<NewsPO> filterQueryWrapper=new QueryWrapper<>();
        filterQueryWrapper.select("*");
        if(!category.isEmpty()){
            filterQueryWrapper.in("category",category);
        }
        if(startTime!=null&&endTime!=null){
            filterQueryWrapper.between("source_time",startTime,endTime);
        }else if(startTime!=null){
            filterQueryWrapper.ge("source_time",startTime);
        }else if(endTime!=null){
            filterQueryWrapper.le("source_time",endTime);
        }
        Page<NewsPO> page= newsMapper.selectPage(new Page<>(pageNo,pageSize),filterQueryWrapper);
        return toNewsVO(page.getRecords());
    }
    @Override
    public List<NewsItemVO> searchNewsByTitle(String title){
        QueryWrapper<NewsPO> searchQueryWrapper=new QueryWrapper<>();
        searchQueryWrapper.select("*");
        searchQueryWrapper.like("title",title);
        List<NewsPO> newsList=newsMapper.selectList(searchQueryWrapper);
        return toNewsVO(newsList);
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
                        .origin_source(newsPO.getOriginSource())
                        .source_time(newsPO.getSourceTime())
                        .category(CategoryType.getCategoryType(newsPO.getCategory()).getCategoryEN())
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
                .origin_source(newsPO.getOriginSource())
                .source_time(newsPO.getSourceTime())
                .link(newsPO.getLink())
                .source_link(newsPO.getSourceLink())
                .category(CategoryType.getCategoryType(newsPO.getCategory()).getCategoryEN())
                .createTime(newsPO.getCreateTime())
                .updateTime(newsPO.getUpdateTime())
                .build();
    }
    /**
     * 将新闻NewNews对象转换为新闻PO
     * @param newNews 新闻NewNews对象
     * @return 新闻PO
     */
    private NewsPO toNewsPO(NewNews newNews){
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
}
