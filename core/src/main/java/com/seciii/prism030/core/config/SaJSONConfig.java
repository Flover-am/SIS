package com.seciii.prism030.core.config;

import cn.hutool.json.JSONConfig;
import org.springframework.stereotype.Component;

/**
 * SaToken异常返回json格式设置
 *
 * @author xueruichen
 * @date 2024.03.10
 */
@Component
public class SaJSONConfig {
    /**
     * 单例模式
     */
    private static final JSONConfig INSTANCE = new JSONConfig();

    private SaJSONConfig() {
        // 不忽略空值字段
        INSTANCE.setIgnoreNullValue(false);
    }

    public static JSONConfig getJsonConfig() {
        return INSTANCE;
    }
}
