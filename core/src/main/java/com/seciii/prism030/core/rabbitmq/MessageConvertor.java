package com.seciii.prism030.core.rabbitmq;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import org.apache.commons.text.StringEscapeUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageConvertor {


    public static NewNews parseJsonToNewNews(String jsonString) {

//        return JSON.parseObject(jsonString,NewNews.class);

        JSONObject jsonObject = JSON.parseObject(jsonString);

        // 获取属性值示例
        String title = jsonObject.getString("title");
        String realTitle = StringEscapeUtils.unescapeJava(title);
        String content = jsonObject.getString("content");
        String realContent = StringEscapeUtils.unescapeJava(content);

        String originSource = jsonObject.getString("originSource");
        String realOriginSource = StringEscapeUtils.unescapeJava(originSource);

        String link = jsonObject.getString("link");

        String sourceLink = jsonObject.getString("sourceLink");

        String sourceTimeStr = jsonObject.getString("sourceTime");
        sourceTimeStr = sourceTimeStr + ":00";

        String categoryStr = jsonObject.getString("category");
        String realCategory = StringEscapeUtils.unescapeJava(categoryStr);



        NewNews newNews = new NewNews();
        newNews.setTitle(realTitle); // 设置标题
        newNews.setContent(realContent); // 设置内容
        newNews.setOriginSource(realOriginSource); // 设置来源
        newNews.setSourceTime(sourceTimeStr); // 设置时间
        newNews.setLink(link); // 设置链接
        newNews.setSourceLink(sourceLink); // 设置源链接
        newNews.setCategory(realCategory); // 设置分类

        return newNews;
    }

    public static NewsPO parseJsonToNewsPO(String jsonString)
    {
        JSONObject jsonObject = JSON.parseObject(jsonString);

        // 获取属性值示例
        String title = jsonObject.getString("title");
        String content = jsonObject.getString("content");
        String originSource = jsonObject.getString("originSource");
        String link = jsonObject.getString("link");
        String sourceLink = jsonObject.getString("sourceLink");
        String sourceTimeStr = jsonObject.getString("sourceTime");
        String categoryStr = jsonObject.getString("category");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime sourceTime = LocalDateTime.parse(sourceTimeStr, formatter);


        return  NewsPO.builder()
                .title(title) // 设置标题
                .content(content) // 设置内容
                .originSource(originSource) // 设置来源
                .sourceTime(sourceTime) // 设置时间
                .link(link) // 设置链接
                .sourceLink(sourceLink) // 设置源链接
                .category(1) // 设置分类
                .createTime(LocalDateTime.now()) // 设置创建时间
                .updateTime(LocalDateTime.now()) // 设置更新时间
                .build(); // 构建NewsPO对象
    }




}
