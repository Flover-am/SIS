package com.seciii.prism030.core.aspect.persistence;

import com.seciii.prism030.core.pojo.po.news.NewsPO;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveCallback;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.seciii.prism030.core.utils.NewsUtil.CREATE_TIME;
import static com.seciii.prism030.core.utils.NewsUtil.UPDATE_TIME;

/**
 * 新闻PO对象在保存前的回调函数实现类
 *
 * @author wang mingsong
 * @date 2024.03.23
 */
@Component
public class NewsPOBeforeSaveCallbackImpl implements BeforeSaveCallback<NewsPO> {
    /**
     * 在保存前的回调函数
     * 用于向文档和实例化对象中添加时间戳
     *
     * @param newsPO     新闻PO
     * @param document   文档
     * @param collection 集合名
     * @return 添加过时间戳的新闻PO
     */
    @Override
    public NewsPO onBeforeSave(NewsPO newsPO, Document document, String collection) {
        LocalDateTime now = LocalDateTime.now();
        if (newsPO.getCreateTime() == null) {
            newsPO.setCreateTime(now);
        }
        if (!document.containsKey(CREATE_TIME)) {
            document.put(CREATE_TIME, now);
        }
        document.put(UPDATE_TIME, now);
        newsPO.setUpdateTime(now);
        return newsPO;
    }

}
