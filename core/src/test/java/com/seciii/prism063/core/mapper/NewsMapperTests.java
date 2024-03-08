package com.seciii.prism063.core.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.seciii.prism063.core.pojo.po.NewsPO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;


import java.time.LocalDateTime;
import java.util.List;

@MybatisPlusTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NewsMapperTests {
    @Autowired
    private NewsMapper newsMapper;
    @BeforeAll
    static void beforeAll(){
        System.out.println("Start testing NewsMapper");
    }
    @Test
    void insertTest(){
        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*")
                .eq("title","test0");
        newsMapper.delete(queryWrapper);
        newsMapper.insert(
                NewsPO.builder()
                        .title("test0")
                        .content("test0content")
                        .category(0)
                        .originSource("CNN")
                        .sourceTime(LocalDateTime.now())
                        .link("www.test0.com")
                        .sourceLink("www.test0source.com")
                        .build()
        );
    }
    @Test
    void selectTest(){
        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*");
        List<NewsPO> newsPO = newsMapper.selectList(queryWrapper);
        for(NewsPO news:newsPO){
            System.out.println(news);
        }
    }
}
