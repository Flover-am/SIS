package com.seciii.prism063.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.seciii.prism063")
@MapperScan("com.seciii.prism063.core.mapper")
public class Prism063Application {

    public static void main(String[] args) {
        SpringApplication.run(Prism063Application.class, args);
    }

}
