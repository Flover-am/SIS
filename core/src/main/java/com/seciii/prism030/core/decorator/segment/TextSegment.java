package com.seciii.prism030.core.decorator.segment;

import com.alibaba.fastjson.JSON;
import com.seciii.prism030.core.enums.SpeechPart;
import com.seciii.prism030.core.pojo.dto.NewsWordDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
        String url = segmentBaseUrl + urlAffix + segMethod;
        String body = postResponseJsonStr(url, text);
        Map<String, Object> map = JSON.parseObject(body);
        if (map == null) {
            return new String[0];
        }
        List<String> list = (List<String>) map.get("data");
        return list.toArray(new String[0]);
    }

    /**
     * 对文本进行分词+实体识别+语义排名
     *
     * @param text 待分词文本
     * @return 分词+实体识别+语义排名结果
     */
    public NewsWordDetail[] rank(String text) {
        String url = segmentBaseUrl + urlAffix + rankMethod;
        String body = postResponseJsonStr(url, text);
        Map<String, Object> map = JSON.parseObject(body);
        if (map == null) {
            return null;
        }
        List<List<?>> list = (List<List<?>>) map.get("data");
        NewsWordDetail[] res = new NewsWordDetail[list.get(0).size()];
        for (int i = 0; i < list.get(0).size(); i++) {
            res[i] = NewsWordDetail.builder()
                    .text((String) (list.get(0)).get(i))
                    .partOfSpeech(SpeechPart.ofTag((String) (list.get(1)).get(i)))
                    .rank((int) (list.get(2)).get(i))
                    .build();
        }
        return res;
    }

    /**
     * 获取分词Http结果
     *
     * @param url      访问
     * @param queryStr 查询字符串
     * @return Http结果
     */
    private String postResponseJsonStr(String url, String queryStr) {
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.set("query", queryStr);
        return restTemplate.postForEntity(url, queryParam, String.class).getBody();
    }

}
