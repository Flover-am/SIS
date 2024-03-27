package com.secii.prism030.core.mapper;

import com.seciii.prism030.core.dao.news.NewsDAOMongo;

import com.seciii.prism030.core.pojo.po.news.NewsPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = NewsDAOMongoImplTest.class)
@ComponentScan("com.seciii.prism030.core")
public class NewsDAOMongoImplTest {

    private NewsDAOMongo newsDAOMongo;
    @Autowired
    public void setNewsDAOMongo(NewsDAOMongo newsDAOMongo) {
        this.newsDAOMongo = newsDAOMongo;
    }
    @Test
    public void testInsert(){
        for(int i=0;i<10;i++){
            NewsPO newsPO=NewsPO.builder()
                    .id((long)i)
                    .title("testTitle"+i)
                    .content("testContent"+i)
                    .originSource("testSource"+i)
                    .category(i%4)
                    .link("testLink"+i)
                    .sourceLink("testOriginLink"+i)
                    .sourceTime(LocalDateTime.now())
                    .build();
            assertEquals(newsPO.getId(),newsDAOMongo.insert(newsPO).getId());
        }
    }
    @Test
    public void testGetNewsById(){

    }
    @Test
    public void testUpdateNewsTitle(){
        assertEquals(0,newsDAOMongo.updateNewsTitle(0L,"alphabetasigma"));
    }
    @Test
    public void testBatchDelete(){
        List<Long> ids= List.of(0L,1L,2L,3L,4L,5L,6L,7L,8L,9L);
        assertEquals(10,newsDAOMongo.batchDeleteNews(ids));
        assertEquals(0,newsDAOMongo.batchDeleteNews(ids));
    }
    @Test
    public void testNextId(){
        newsDAOMongo.getNextNewsId();
    }
    @Test
    public void testFilter(){
        for(int i=0;i<3;i++){
            List<NewsPO> list=newsDAOMongo.searchFilteredNewsByPage(
                    2,i,
                    "test",
                    List.of(0,1,2),
                    null,null,null
            );
            list.forEach(System.out::println);
        }
    }
    @Test
    public void testCount(){
        List<NewsPO> list=newsDAOMongo.searchFilteredNewsByPage(
                -1,-1,
                "test",
                List.of(0,1,2),
                null,null,null
        );
        list.forEach(System.out::println);
        System.out.println(list.size());
        Long c=newsDAOMongo.getSearchedFilteredNewsCount("test",
                List.of(0,1,2),
                null,null,null);
        System.out.println(c);
    }

}
