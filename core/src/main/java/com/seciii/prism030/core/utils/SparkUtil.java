package com.seciii.prism030.core.utils;

import com.seciii.prism030.common.exception.NewsException;
import com.seciii.prism030.common.exception.error.ErrorType;
import com.unfbx.sparkdesk.SparkDeskClient;
import com.unfbx.sparkdesk.entity.*;
import com.unfbx.sparkdesk.listener.ChatListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private static final String PROMPT = "你是一名从新闻提取实体关系的信息专家，请你先从后面的input中提取关键事件，再分析其中的实体关系三元组（如果很多则只留取最重要的十条）\n" +
            " 按照以下格式返回实体关系三元组列表：(主体 | 关系 | 客体）\n" +
            "(主体 | 关系 | 客体)\n" +
            " 请注意，实体为（人物、地点、组织或物体等，可附带描述），实体关系要清晰明确无歧义。如果文本中没有明显的三元组或者任务失败，请返回NULL\n" +
            " 例子如下：\n" +
            "exampleinput:\n" +
            " 4月14日，第42届香港电影金像奖举行颁奖典礼，梁朝伟凭借《金手指》第六次拿到影帝！\n" +
            "此前五次分别是：第14届《重庆森林》（1995年），第17届《春光乍泄》（1998年），第20届《花样年华》（2001年），第22届《无间道》（2003年），第24届《2046》（2005年）。\n" +
            "不过，梁朝伟因为忙于排片，没能现场领奖。\n" +
            "对于这一史无前例的成就，不少网友大赞梁朝伟封神，但也有网友质疑梁朝伟在这个大烂片里演技浮夸，简直是生涯最差演技时刻。\n" +
            "另外，《毒舌律师》获得最佳影片奖，郑保瑞以《命案》获得最佳导演奖，余香凝、姜大卫以《白日之下》分别获得最佳女主角奖、最佳男配角奖。\n" +
            " exampleoutput:(第42届香港电影金像奖 | 举行时间 | 4月14日)\n" +
            "(梁朝伟 | 获奖作品 | 《金手指》\n" +
            "(梁朝伟 | 获得 | 金像奖影帝) | (梁朝伟 | 曾获得 | 第14届《重庆森林》影帝)\n" +
            "(梁朝伟 | 曾获得 | 第17届《春光乍泄》影帝)\n" +
            "(梁朝伟 | 曾获得 | 第20届《花样年华》影帝)\n" +
            "(梁朝伟 | 曾获得 | 第22届《无间道》影帝)\n" +
            "(梁朝伟 | 因为 | 忙于排片没能现场领奖) \n" +
            "(《毒舌律师》 | 获得 | 最佳影片奖)\n" +
            "(郑保瑞 | 获得 | 最佳导演奖) \n" +
            " input：\n" +
            "%s";
    @Value("${spark.appid}")
    private String APP_ID;
    @Value("${spark.apisecret}")
    private String API_SECRET;
    @Value("${spark.apikey}")
    private String API_KEY;

    public String chat(String content) {
        String appid = APP_ID;
        String apiSecret = API_SECRET;
        String apiKey = API_KEY;
        System.out.println(appid);
        System.out.println(apiSecret);
        System.out.println(apiKey);
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
