package com.seciii.prism030.core.rabbitmq;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import org.apache.commons.text.StringEscapeUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageConvertor {

    /**
     * 将json格式新闻映射为NewNews对象
     * @param jsonString
     * @return newNews
     */

    public static NewNews parseJsonToNewNews(String jsonString) {


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

}
