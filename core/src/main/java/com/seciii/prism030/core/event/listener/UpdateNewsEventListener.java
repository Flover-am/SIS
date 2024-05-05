package com.seciii.prism030.core.event.listener;

import com.seciii.prism030.core.controller.SseController;
import com.seciii.prism030.core.event.UpdateNewsEvent;
import com.seciii.prism030.core.utils.NewsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 更新新闻事件监听器
 *
 * @author wang mingsong
 * @date 2024.04.30
 */
@Component
public class UpdateNewsEventListener implements ApplicationListener<UpdateNewsEvent> {
    @Autowired
    private SseController sseController;

    /**
     * 新闻更新事件响应方法
     *
     * @param event 需要处理的事件
     */
    @Override
    public void onApplicationEvent(UpdateNewsEvent event) {
        sseController.sendEvents(event.getNewsVO());
    }
}
