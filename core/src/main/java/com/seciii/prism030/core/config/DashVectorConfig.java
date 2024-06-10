package com.seciii.prism030.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * DashVector配置类
 *
 * @author xueruichen
 * @date 2024.05.28
 */
@Configuration
public class DashVectorConfig {
    @Value("${dashvector.apikey}")
    private String apiKey;
    @Value("${dashvector.endpoint}")
    private String endpoint;

    public String getApiKey() {
        return apiKey;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
