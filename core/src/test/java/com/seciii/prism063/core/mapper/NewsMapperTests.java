package com.seciii.prism063.core.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.seciii.prism063.core.pojo.po.NewsPO;
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
    @Test
    void insertTest(){
        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*");
        newsMapper.delete(queryWrapper);
        for(int i=0;i<10;i++){
            newsMapper.insert(
                    NewsPO.builder()
                            .title("test"+i)
                            .content("test"+i+"content")
                            .category(i/5)
                            .originSource("CNN")
                            .sourceTime(LocalDateTime.now())
                            .link("www.test"+i+".com")
                            .sourceLink("www.test"+i+"source.com")
                            .build()
            );
        }
    }
    @Test
    void selectTest(){
        QueryWrapper<NewsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*");
        List<NewsPO> newsPO = newsMapper.selectList(queryWrapper);
        for(NewsPO news:newsPO){
            System.err.println(news);
        }
    }
}
