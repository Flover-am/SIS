package com.seciii.prism063.core.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
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
                    SaRouter.match("/user/**")
                            .notMatch("/user/login")
                            .notMatch("/user/register")
                            .check(StpUtil::checkLogin);
                    SaRouter.match("/admin/**")
                            .notMatch("/admin/login")
                            .check(StpUtil::checkLogin)
                            .check(() -> StpUtil.checkRoleOr("super-admin", "news-admin"));
                });
    }
}
