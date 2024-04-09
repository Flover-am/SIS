package com.seciii.prism030.core.utils;

import com.seciii.prism030.common.exception.NewsException;
import com.seciii.prism030.common.exception.error.ErrorType;
import com.unfbx.sparkdesk.SparkDeskClient;
import com.unfbx.sparkdesk.entity.*;
import com.unfbx.sparkdesk.listener.ChatListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 星火大模型工具类
 *
 * @author xueruichen
 * @date 2024.04.09
 */
@Component
@Slf4j
public class SparkUtil {
    private static final String SPARK_API_HOST_WSS_V3 = "https://spark-api.xf-yun.com/v3.5/chat";
    private static final String PROMPT = "给定以下文本，请分析并提取其中的关系三元组，数量不超过10个。\n" +
            "每个三元组应该包括主体（人物、地点、组织或物体）、关系（少于8个字）和客体（人物、地点、组织或物体）。\n" +
            "如果文本中没有明显的关系，输出NULL。\n" +
            "\n" +
            "文本： “%s”\n" +
            "\n" +
            "请按照以下格式提取关系三元组列表：\n" +
            "(主体 | 关系 | 客体)\n" +
            "(主体 | 关系 | 客体)\n" +
            "如果没有可识别的关系，请输出空字符串。不允许输出其他任何内容！\n" +
            "请注意，主体和客体只包括人物、地点、组织或物体，必须是一个名次，不允许出现包括动词、行为、事件在内的其他内容。\n" +
            "请注意，输出的关系必须少于8个字。";
//    private static Environment environment;
    public static String chat(String content) {
        String appid = "";
        String apiSecret = "";
        String apiKey = "";
        SparkDeskClient sparkDeskClient = SparkDeskClient.builder()
                .host(SPARK_API_HOST_WSS_V3)
                .appid(appid)
                .apiSecret(apiSecret)
                .apiKey(apiKey)
                .build();
        //构建请求参数
        InHeader header = InHeader.builder().uid(UUID.randomUUID().toString().substring(0, 10)).appid(appid).build();
        Parameter parameter = Parameter.builder().chat(Chat.builder().domain("generalv3").maxTokens(2048).temperature(0.3).build()).build();
        List<Text> text = new ArrayList<>();
        text.add(Text.builder().role(Text.Role.USER.getName()).content(String.format(PROMPT, content)).build());
        InPayload payload = InPayload.builder().message(Message.builder().text(text).build()).build();
        AIChatRequest aiChatRequest = AIChatRequest.builder().header(header).parameter(parameter).payload(payload).build();

        CompletableFuture<String> resultFuture = new CompletableFuture<>();
        StringBuilder resultBuilder = new StringBuilder();

        //发送请求
        sparkDeskClient.chat(new ChatListener(aiChatRequest) {
            //异常回调
            @SneakyThrows
            @Override
            public void onChatError(AIChatResponse aiChatResponse) {
                log.warn(String.valueOf(aiChatResponse));
                throw new NewsException(ErrorType.ILLEGAL_ARGUMENTS);
            }

            //输出回调
            @Override
            public void onChatOutput(AIChatResponse aiChatResponse) {
                log.info("content: " + aiChatResponse);
                resultBuilder.append(aiChatResponse.getPayload().getChoices().getText().get(0).getContent());
            }

            //会话结束回调
            @Override
            public void onChatEnd() {
                log.info("会话结束");
                resultFuture.complete(resultBuilder.toString());
            }

            //会话结束 获取token使用信息回调
            @Override
            public void onChatToken(Usage usage) {
                log.info("token 信息：" + usage);
            }
        });

        // 等待 CompletableFuture 完成
        try {
            return resultFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new NewsException(ErrorType.ILLEGAL_ARGUMENTS);
        }
    }
}
