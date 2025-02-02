package com.seciii.prism030.core.service;

import com.seciii.prism030.core.pojo.dto.UpdatedNewsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class SseService {
    private final List<SseEmitter> newsUpdateEmitterList = new CopyOnWriteArrayList<>();
    private final Map<String,SseEmitter> chatEmitterMap = new HashMap<>();

    /**
     * 订阅新闻更新文本流
     *
     * @return 新闻更新的字符流
     */
    public SseEmitter subscribeNewsUpdate(){
        SseEmitter emitter=new SseEmitter(Long.MAX_VALUE);
        newsUpdateEmitterList.add(emitter);
        log.info("New subscriber for news update event.");
        emitter.onTimeout(()->{
            newsUpdateEmitterList.remove(emitter);
            log.info("News update emitter timeout.");
        });
        return emitter;
    }

    /**
     * 获取AI聊天文本流的sse对象
     *
     * @param sessionCode 聊天对应的sessionCode
     * @return 聊天的字符流
     */
    public SseEmitter getChatStreamEmitter(String sessionCode){
        SseEmitter emitter=new SseEmitter();
        chatEmitterMap.put(sessionCode,emitter);
        return emitter;
    }

    /**
     * 发送聊天消息
     *
     * @param sessionCode 聊天对应的sessionCode
     * @param message 聊天消息
     */
    public void sendChatMessage(String sessionCode,String message){
        SseEmitter emitter=chatEmitterMap.get(sessionCode);
        if(emitter!=null){
            try{
                emitter.send(message);
            }catch (IOException e){
                emitter.complete();
            }
        }
    }

    /**
     * 发送新闻更新事件
     *
     * @param updatedNewsDTO 新闻更新时间DTO
     */
    public void sendUpdateNewsEvent(UpdatedNewsDTO updatedNewsDTO){
        if(newsUpdateEmitterList.isEmpty()){
            log.info("No subscriber for news update event.");
        }
        for(SseEmitter emitter:newsUpdateEmitterList){
            try{
                emitter.send(updatedNewsDTO);
            }catch (IOException e){
                emitter.complete();
                newsUpdateEmitterList.remove(emitter);
            }catch (IllegalStateException e){
                e.printStackTrace();
                emitter.complete();
                newsUpdateEmitterList.remove(emitter);
            }
        }
    }

}
