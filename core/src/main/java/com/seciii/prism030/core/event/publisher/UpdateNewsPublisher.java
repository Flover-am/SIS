package com.seciii.prism030.core.event.publisher;

import com.seciii.prism030.core.enums.UpdateType;
import com.seciii.prism030.core.event.UpdateNewsEvent;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class UpdateNewsPublisher {
    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * 发布更新新闻事件
     *
     * @param source 发布者
     * @param newsPO 待发布的修改后新闻
     */
    public void publishModifiedNewsEvent(Object source, @NotNull NewsPO newsPO, UpdateType updateType) {
        publisher.publishEvent(new UpdateNewsEvent(source, newsPO, updateType));
    }
}
