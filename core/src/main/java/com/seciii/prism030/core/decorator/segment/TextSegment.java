package com.seciii.prism030.core.decorator.segment;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static com.seciii.prism030.core.decorator.segment.SegmentConstants.*;


/**
 * 中文分词器
 *
 * @author wang mingsong
 * @date 2024.04.13
 */
@Component
public class TextSegment {
    private RestTemplate restTemplate;

    @Value("${segment.baseurl}")
    private String segmentBaseUrl;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 对文本进行分词
     *
     * @param text 待分词文本
     * @return 分词结果
     */
    public String[] segment(String text) {
        String url = segmentBaseUrl + urlAffix + segMethod + "?query=" + text;
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        String body = restTemplate.getForEntity(url, String.class).getBody();
        Map<String, Object> map = JSON.parseObject(body);
        if (map == null) {
            return new String[0];
        }
        List<String> list = (List<String>) map.get("data");
        return list.toArray(new String[0]);
    }

}
