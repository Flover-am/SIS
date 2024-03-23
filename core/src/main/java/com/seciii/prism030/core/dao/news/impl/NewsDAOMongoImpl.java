package com.seciii.prism030.core.dao.news.impl;


import com.seciii.prism030.core.dao.news.NewsDAOMongo;
import com.seciii.prism030.core.pojo.po.news.NewsPO;

import static com.seciii.prism030.core.utils.NewsUtil.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

/**
 * MongoDB的新闻DAO接口实现类
 *
 * @author wang mingsong
 * @date 2024.03.23
 */
@Component
public class NewsDAOMongoImpl implements NewsDAOMongo {

    private final static String COLLECTION_NAME = "news";

    private MongoTemplate mongoTemplate;

    @Autowired
    @Lazy
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 获取新闻详情
     *
     * @param id 新闻id
     * @return 新闻PO
     */
    @Override
    public NewsPO getNewsById(Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ID).is(id));
        return mongoTemplate.findOne(query, NewsPO.class, COLLECTION_NAME);
    }

    /**
     * 插入新闻
     *
     * @param newsPO 新闻PO
     * @return 插入成功返回新闻PO，否则返回null
     */
    @Override
    public NewsPO insert(NewsPO newsPO) {
        return mongoTemplate.insert(newsPO, COLLECTION_NAME);
    }

    /**
     * 删除新闻
     *
     * @param newsPO 新闻PO
     * @return 删除成功返回0，否则返回-1
     */
    @Override
    public int delete(NewsPO newsPO) {
        long deleteCount = mongoTemplate.remove(newsPO, COLLECTION_NAME).getDeletedCount();
        return deleteCount > 0 ? 0 : -1;
    }

    /**
     * 根据id删除新闻
     *
     * @param id 新闻id
     * @return 删除成功返回0，否则返回-1
     */
    @Override
    public int deleteById(Long id) {
        long deleteCount = mongoTemplate.remove(Query.query(Criteria.where(ID).is(id)), NewsPO.class, COLLECTION_NAME).getDeletedCount();
        return deleteCount > 0 ? 0 : -1;
    }

    /**
     * 修改新闻标题
     *
     * @param id    新闻id
     * @param title 新标题
     * @return 修改成功返回0，否则返回-1
     */
    @Override
    public int updateNewsTitle(Long id, String title) {
        return modifyById(id, Update.update(TITLE, title));
    }

    /**
     * 修改新闻内容
     *
     * @param id      新闻id
     * @param content 新内容
     * @return 修改成功返回0，否则返回-1
     */
    @Override
    public int updateNewsContent(Long id, String content) {
        return modifyById(id, Update.update(CONTENT, content));
    }

    /**
     * 修改新闻来源
     *
     * @param id     新闻id
     * @param source 新来源
     * @return 修改成功返回0，否则返回-1
     */
    @Override
    public int updateNewsSource(Long id, String source) {
        return modifyById(id, Update.update(SOURCE, source));
    }


    /**
     * 批量删除新闻
     *
     * @param ids 新闻id列表
     * @return 返回成功删除的条目数
     */
    @Override
    public Long batchDeleteNews(List<Long> ids) {
        Query query = Query.query(Criteria.where("id").in(ids));
        return mongoTemplate.remove(query, NewsPO.class).getDeletedCount();
    }

    /**
     * 获取筛选后的新闻条目数
     *
     * @param category     新闻类别
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param originSource 新闻来源
     * @return 新闻条目数
     */
    @Override
    public Long getFilteredNewsCount(List<Integer> category, LocalDateTime startTime, LocalDateTime endTime, String originSource) {
        Query query = getQuery(-1, 0, null, category, startTime, endTime, originSource);
        return getCountByQuery(query);
    }

    /**
     * 获取筛选后的新闻列表
     *
     * @param pageSize     页码下标
     * @param pageOffset   页偏移大小
     * @param category     新闻类别
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param originSource 新闻来源
     * @return 新闻PO列表
     */
    @Override
    public List<NewsPO> getFilteredNewsByPage(int pageSize, int pageOffset, List<Integer> category, LocalDateTime startTime, LocalDateTime endTime, String originSource) {
        Query query = getQuery(pageSize, pageOffset, null, category, startTime, endTime, originSource);
        return getItemsByQuery(query);
    }

    /**
     * 按标题模糊搜索并筛选后的新闻条目
     *
     * @param pageSize     页码下标
     * @param pageOffset   页偏移大小
     * @param title        新闻标题
     * @param category     新闻类别
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param originSource 新闻来源
     * @return 新闻PO列表
     */
    @Override
    public List<NewsPO> searchFilteredNewsByPage(int pageSize, int pageOffset, String title, List<Integer> category, LocalDateTime startTime, LocalDateTime endTime, String originSource) {
        Query query = getQuery(pageSize, pageOffset, title, category, startTime, endTime, originSource);
        return getItemsByQuery(query);
    }

    /**
     * 按标题模糊搜索并筛选后的新闻条目数
     *
     * @param title        新闻标题
     * @param category     新闻类别
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param originSource 新闻来源
     * @return 新闻条目数
     */
    @Override
    public Long getSearchedFilteredNewsCount(String title, List<Integer> category, LocalDateTime startTime, LocalDateTime endTime, String originSource) {
        Query query = getQuery(-1, 0, title, category, startTime, endTime, originSource);
        return getCountByQuery(query);
    }


    /**
     * 获取下一个可用的新闻id
     *
     * @return 下一个可用的新闻id
     */
    @Override
    public Long getNextNewsId() {
        GroupOperation groupMaxId = Aggregation.group().max(ID).as("maxId");
        Aggregation aggregation = Aggregation.newAggregation(groupMaxId);
        MaxId result = mongoTemplate.aggregate(aggregation, COLLECTION_NAME, MaxId.class).getUniqueMappedResult();

        if (result == null) {
            return 0L;
        }
        return result.getMaxId() + 1L;
    }

    /**
     * 按id查找并由update修改新闻
     *
     * @param id     新闻id
     * @param update 修改操作
     * @return 修改成功返回0，否则返回-1
     */
    private int modifyById(Long id, Update update) {
        update.set(UPDATE_TIME, LocalDateTime.now());
        NewsPO newsPO = mongoTemplate.findAndModify(Query.query(Criteria.where(ID).is(id)), update, NewsPO.class, COLLECTION_NAME);
        return newsPO == null ? -1 : 0;
    }

    /**
     * 根据查询条件获取查询对象
     *
     * @param pageSize     页码大小
     * @param pageOffset   页码偏移
     * @param title        新闻标题
     * @param category     新闻类别
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param originSource 新闻来源
     * @return 查询对象
     */
    private Query getQuery(
            int pageSize,
            int pageOffset,
            String title,
            List<Integer> category,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String originSource
    ) {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, SOURCE_TIME));
        if (title != null && !title.isEmpty()) {
            query.addCriteria(Criteria.where(TITLE).regex(getRegex(title)));
        }
        if (category != null && !category.isEmpty()) {
            query.addCriteria(Criteria.where(CATEGORY).in(category));
        }
        if (startTime != null) {
            query.addCriteria(Criteria.where(SOURCE_TIME).gte(startTime));
        }
        if (endTime != null) {
            query.addCriteria(Criteria.where(SOURCE_TIME).lte(endTime));
        }
        if (originSource != null && !originSource.isEmpty()) {
            query.addCriteria(Criteria.where(SOURCE).regex(getRegex(originSource)));
        }
        if (pageSize > 0) {
            if (pageOffset < 0) pageOffset = 0;
            query.skip(pageOffset).limit(pageSize);
        }
        return query;
    }

    /**
     * 根据查询条件获取新闻数量
     *
     * @param query 查询条件
     * @return 新闻数量
     */
    private Long getCountByQuery(Query query) {
        return mongoTemplate.count(query, NewsPO.class);
    }

    /**
     * 根据查询条件获取新闻列表
     *
     * @param query 查询条件
     * @return 新闻列表
     */
    private List<NewsPO> getItemsByQuery(Query query) {
        return mongoTemplate.find(query, NewsPO.class);
    }

    /**
     * 包装后的新闻最大id类
     */
    @Getter
    @Setter
    private static class MaxId {
        private Long maxId;
    }

    /**
     * 简单的正则表达式，在任意位置匹配任意字一次或零次
     *
     * @param s 待转换字符串
     * @return 转换后的正则表达式
     */
    private Pattern getRegex(String s) {
        if (s == null || s.isEmpty()) {
            return Pattern.compile("^.*$");
        }
        String regex = "^(?:.*[" + s + "]+).*" + String.join("?", s.split("")) + "?.*$";
        return Pattern.compile(regex);
    }

}
