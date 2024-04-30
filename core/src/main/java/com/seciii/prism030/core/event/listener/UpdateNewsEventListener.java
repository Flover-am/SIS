package com.seciii.prism030.core.event.listener;

import com.seciii.prism030.core.controller.SseController;
import com.seciii.prism030.core.event.UpdateNewsEvent;
import com.seciii.prism030.core.utils.NewsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UpdateNewsEventListener implements ApplicationListener<UpdateNewsEvent> {
    @Autowired
    private SseController sseController;
    @Override
    public void onApplicationEvent(UpdateNewsEvent event) {
        sseController.sendEvents(NewsUtil.toNewsVO(event.getNewsPO()));
    }
}
