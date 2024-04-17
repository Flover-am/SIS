package com.seciii.prism030.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xueruichen
 *
 * @date 2024.03.14
 */
@SpringBootApplication(scanBasePackages = "com.seciii.prism030")
@MapperScan("com.seciii.prism030.core.mapper")
public class Prism030Application {

    public static void main(String[] args) {
        SpringApplication.run(Prism030Application.class, args);
    }

}
