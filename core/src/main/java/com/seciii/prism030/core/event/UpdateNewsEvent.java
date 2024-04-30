package com.seciii.prism030.core.event;

import com.seciii.prism030.core.enums.UpdateType;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 修改新闻事件
 *
 * @author wangmingsong
 * @date 2024.04.30
 */
public class UpdateNewsEvent extends ApplicationEvent {
    @Getter
    private final NewsPO newsPO;
    @Getter
    private final UpdateType updateType;

    public UpdateNewsEvent(Object source, NewsPO newsPO, UpdateType updateType) {
        super(source);
        this.newsPO = newsPO;
        this.updateType = updateType;
    }

}
