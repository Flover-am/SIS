package com.seciii.prism063.core.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.seciii.prism063.common.Result;
import com.seciii.prism063.common.enums.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SaToken路由拦截器注册
 *
 * @author xueruichen
 * @date 2024.03.04
 */
@Slf4j
@Configuration
public class SaTokenConfigure {
    /**
     * 注册 [Sa-Token全局过滤器]
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                // 指定 拦截路由 与 放行路由
                .addInclude("/**")
                // 认证函数: 每次请求执行
                .setAuth(obj -> {
                    // 登录认证 -- 拦截所有路由，并排除用于开放注册和登陆的接口
                    SaRouter.match("/**/user/**")
                            .notMatch("/**/user/login")
                            .notMatch("/**/user/register")
                            .check(StpUtil::checkLogin);
                    SaRouter.match("/**/admin/**")
                            .notMatch("/**/admin/login")
                            .check(StpUtil::checkLogin)
                            .check(() -> StpUtil.checkRoleOr("super-admin", "news-admin"));
                })
                // 返回异常结果
                .setError(e -> {
                    log.error(e.getMessage());
                    e.printStackTrace();
                    // 设置响应头
                    SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
                    return JSONUtil.toJsonStr(Result.error(ErrorType.UNAUTHORIZED.getCode(), "token验证失败，请重新登陆"), SaJSONConfig.getJsonConfig());
                });
    }
}
