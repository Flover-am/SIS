package com.seciii.prism030.core.event.listener;

import com.seciii.prism030.core.controller.SseController;
import com.seciii.prism030.core.event.UpdateNewsEvent;
import com.seciii.prism030.core.pojo.dto.UpdatedNewsDTO;
import com.seciii.prism030.core.pojo.vo.news.NewsVO;
import com.seciii.prism030.core.service.NewsService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UpdateNewsEventListener implements ApplicationListener<UpdateNewsEvent> {
    @Autowired
    private SseController sseController;

    @Autowired
    private NewsService newsService;

    /**
     * 新闻更新事件响应方法
     *
     * @param event 新闻更新事件
     */
    @Override
    public void onApplicationEvent(UpdateNewsEvent event) {
        switch (event.getUpdateType()) {
            case ADD:
                addEvent(event);
                break;
            case MODIFY:
                modifyEvent(event);
                break;
            case DELETE:
                deleteEvent(event);
                break;
            default:
                return;
        }
    }

    /**
     * 添加新闻事件响应方法
     *
     * @param event 新闻更新事件：添加
     */
    private void addEvent(UpdateNewsEvent event) {

        NewsVO addedNews = event.getNewsVO();
        // sse通知前端
        sseController.sendEvents(new UpdatedNewsDTO(addedNews, event.getUpdateType()));

        // 生成并保存新闻的词云 现已交由Flink
//        try {
//            newsService.generateAndSaveWordCloud(addedNews.getId(), addedNews.getContent());
//        } catch (RestClientException e) {
//            log.error(String.format("Failed to generate word cloud for news %d: %s", addedNews.getId(), e.getMessage()));
//        }
    }

    /**
     * 修改新闻事件响应方法
     *
     * @param event 新闻更新事件：修改
     */
    private void modifyEvent(UpdateNewsEvent event) {
        sseController.sendEvents(new UpdatedNewsDTO(event.getNewsVO(), event.getUpdateType()));
    }

    /**
     * 删除新闻事件响应方法
     *
     * @param event 新闻更新事件：删除
     */
    private void deleteEvent(UpdateNewsEvent event) {
        sseController.sendEvents(new UpdatedNewsDTO(event.getNewsVO(), event.getUpdateType()));
    }
}
