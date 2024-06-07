package com.seciii.prism030.core.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.seciii.prism030.common.Result;
import com.seciii.prism030.common.exception.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                    SaRouter.match("/**/news/**")
                            .matchMethod("GET")
                            .check(StpUtil::checkLogin);
                    SaRouter.match("/**/news/**")
                            .notMatchMethod("GET")
                            .check(() -> StpUtil.checkRoleOr("super-admin", "news-admin"));
                    SaRouter.match("/**/superAdmin/**")
                            .check(() -> StpUtil.checkRoleOr("super-admin"));
                    SaRouter.match("/**/graph/**")
                            .check(StpUtil::checkLogin);
                    SaRouter.match("/**/llm/**")
                            .notMatch("/**/llm")
                            .check(StpUtil::checkLogin);
                })
                // 返回异常结果
                .setError(e -> {
                    log.error(e.getMessage());
                    e.printStackTrace();
                    // 设置响应头
                    SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
                    return JSONUtil.toJsonStr(Result.error(ErrorType.UNAUTHORIZED.getCode(), "token验证失败，请重新登陆"), SaJSONConfig.getJsonConfig());
                })
                // 前置函数：在每次认证函数之前执行
                .setBeforeAuth(obj -> {
                    SaHolder.getResponse()
                            // ---------- 设置跨域响应头 ----------
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", SaHolder.getRequest().getHeader("Origin"))
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                            // 允许的header参数
                            .setHeader("Access-Control-Allow-Headers", "Content-Type,x-requested-with,satoken")
                            // 允许跨域携带cookies
                            .setHeader("Access-Control-Allow-Credentials", "true")
                            // 有效时间
                            .setHeader("Access-Control-Max-Age", "3600")

                    ;
                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .free(r -> log.info("--------OPTIONS预检请求，不做处理"))
                            .back();
                });

    }
}
