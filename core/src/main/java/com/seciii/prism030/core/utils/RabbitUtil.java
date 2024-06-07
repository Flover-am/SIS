package com.seciii.prism030.core.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seciii.prism030.core.pojo.dto.NewsEntityRelationshipDTO;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 处理RabbitMQ消息格式转换的工具类
 */
@Slf4j
public class RabbitUtil {
    public final static String RABBIT_TITLE = "title";
    public final static String RABBIT_CONTENT = "content";
    public final static String RABBIT_SOURCE = "originSource";
    public final static String RABBIT_SOURCE_TIME = "sourceTime";
    public final static String RABBIT_LINK = "link";
    public final static String RABBIT_SOURCE_LINK = "sourceLink";
    public final static String RABBIT_CATEGORY = "category";
    public final static String RABBIT_ER = "er";
    public final static String RABBIT_SEGMENT = "divided";
    public final static String RABBIT_DASH_ID = "dashId";

    /**
     * 将json格式新闻映射为NewNews对象
     *
     * @param jsonString json格式新闻
     * @return NewNews对象
     */
    public static NewNews parseJsonToNewNews(String jsonString) {


        JSONObject jsonObject = JSON.parseObject(jsonString);

        // 获取属性值示例
        String title = jsonObject.getString(RABBIT_TITLE);
        String realTitle = StringEscapeUtils.unescapeJava(title);
        String content = jsonObject.getString(RABBIT_CONTENT);
        String realContent = StringEscapeUtils.unescapeJava(content);

        String originSource = jsonObject.getString(RABBIT_SOURCE);
        String realOriginSource = StringEscapeUtils.unescapeJava(originSource);

        String link = jsonObject.getString(RABBIT_LINK);

        String sourceLink = jsonObject.getString(RABBIT_SOURCE_LINK);

        String sourceTimeStr = jsonObject.getString(RABBIT_SOURCE_TIME);
//        sourceTimeStr = sourceTimeStr + ":00";

        String categoryStr = jsonObject.getString(RABBIT_CATEGORY);
        String realCategory = StringEscapeUtils.unescapeJava(categoryStr);

        JSONArray dashIdJSONArray = jsonObject.getJSONArray(RABBIT_DASH_ID);
        List<Object> dashIdObject = Arrays.asList(dashIdJSONArray.toArray());
        List<String> dashId = new ArrayList<>();
        for (Object obj : dashIdObject) {
            if (obj instanceof String) {
                dashId.add((String) obj);
            }
        }

        NewNews newNews = new NewNews();
        newNews.setTitle(realTitle); // 设置标题
        newNews.setContent(realContent); // 设置内容
        newNews.setOriginSource(realOriginSource); // 设置来源
        newNews.setSourceTime(sourceTimeStr); // 设置时间
        newNews.setLink(link); // 设置链接
        newNews.setSourceLink(sourceLink); // 设置源链接
        newNews.setCategory(realCategory); // 设置分类
        newNews.setDashId(dashId);

        return newNews;
    }


    /**
     * 从json字符串中获取实体关系列表
     *
     * @param jsonStr json字符串
     * @return 实体关系列表
     */
    public static List<NewsEntityRelationshipDTO> getERList(String jsonStr) {
        JSONObject json = JSON.parseObject(jsonStr);
        String ERString = StringEscapeUtils.unescapeJava(json.getString(RABBIT_ER));
        if (ERString == null || ERString.isEmpty()) return null;

        // 按行分割每一个实体关系
        String[] ERArray = ERString.split("\n");

        List<NewsEntityRelationshipDTO> resultList = new ArrayList<>();

        for (String ERItem : ERArray) {
            // 每个实体关系应以"(E1 | R | E2)"格式给出
            ERItem = ERItem.strip();
            if (!ERItem.startsWith("(") || !ERItem.endsWith(")")) {
                log.warn("实体关系格式错误: " + ERItem);
                continue;
            }
            String[] ERItemArray = ERItem.substring(1, ERItem.length() - 1).split("\\|");
            if (ERItemArray.length != 3) {
                log.warn("实体关系格式错误: " + ERItem + " " + ERItemArray.toString());
                continue;
            }
            resultList.add(NewsEntityRelationshipDTO.builder()
                    .entity1(ERItemArray[0].trim())
                    .relationship(ERItemArray[1].trim())
                    .entity2(ERItemArray[2].trim())
                    .build());
        }
        return resultList;
    }

    /**
     * 从json字符串中获取分词结果
     *
     * @param jsonStr json字符串
     * @return 分词结果
     */
    public static List<String> getWordSegment(String jsonStr) {
        JSONObject json = JSON.parseObject(jsonStr);
        return ((JSONArray) json.get(RABBIT_SEGMENT)).toJavaList(String.class);
    }

}
