package com.seciii.prism030.core.utils.persistence;

import com.seciii.prism030.core.classifier.Classifier;
import com.seciii.prism030.core.dao.news.NewsDAOMongo;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.stereotype.Component;

/**
 * 新闻PO对象在转换前的回调函数实现类
 *
 * @author wang mingsong
 * @date 2024.03.23
 */
@Component
public class NewsPOBeforeConvertCallbackImpl implements BeforeConvertCallback<NewsPO> {
    private NewsDAOMongo newsDAOMongo;
    private Classifier classifier;

    @Autowired
    public void setNewsDAOMongo(NewsDAOMongo newsDAOMongo) {
        this.newsDAOMongo = newsDAOMongo;
    }
    @Autowired
    public void setClassifier(Classifier classifier) {
        this.classifier = classifier;
    }

    /**
     * 在转换前的回调函数
     * 用于添加新闻id
     *
     * @param newsPO     新闻PO
     * @param collection 集合名
     * @return 添加过id的新闻PO
     */
    @Override
    public NewsPO onBeforeConvert(NewsPO newsPO, String collection) {
        if (newsPO.getId() == null) {
            newsPO.setId(newsDAOMongo.getNextNewsId());
        }
        if(newsPO.getCategory() == null){
            newsPO.setCategory(classifier.classify(newsPO.getTitle()).toInt());
        }
        return newsPO;
    }

}
