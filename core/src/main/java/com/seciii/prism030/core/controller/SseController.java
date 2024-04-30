package com.seciii.prism030.core.controller;

import com.seciii.prism030.core.pojo.vo.news.NewsVO;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/v1/sse")
public class SseController {
    private final List<SseEmitter> emitterList = new CopyOnWriteArrayList<>();

    /**
     * 订阅新闻更新
     *
     * @return 更新后的新闻的字符流
     */
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        addEmitter(emitter);
        return emitter;
    }

    /**
     * 发送新闻VO事件
     *
     * @param newsVO 新闻VO
     */
    public void sendEvents(NewsVO newsVO) {
        for (SseEmitter emitter : emitterList) {
            try {
                emitter.send(newsVO);
            } catch (IOException e) {
                emitter.complete();
                emitterList.remove(emitter);
            }
        }
    }

    /**
     * 添加SseEmitter
     *
     * @param emitter 待添加的emitter
     */
    public void addEmitter(SseEmitter emitter) {
        emitterList.add(emitter);
        emitter.onCompletion(() -> emitterList.remove(emitter));
        emitter.onTimeout(() -> emitterList.remove(emitter));
    }
}
