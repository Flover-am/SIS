package com.seciii.prism030.core.controller;

import com.seciii.prism030.core.service.SseService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/v1/sse")
public class SseController {
    private final SseService sseService;

    public SseController(SseService sseService){
        this.sseService=sseService;
    }

    /**
     * 获取AI聊天文本流的sse对象
     *
     * @param sessionCode 聊天对应的sessionCode
     * @return 聊天的字符流
     */
    @GetMapping(path="/chat",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeChat(@RequestParam String sessionCode){
        SseEmitter emitter = sseService.getChatStreamEmitter(sessionCode);
        return emitter;
    }


    /**
     * 订阅新闻更新
     *
     * @return 更新后的新闻的字符流
     */
    @GetMapping(path="/newsUpdate",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeNewsUpdate() {
        SseEmitter emitter = sseService.subscribeNewsUpdate();
        return emitter;
    }

}
