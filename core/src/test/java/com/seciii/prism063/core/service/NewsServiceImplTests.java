package com.seciii.prism063.core.service;

import com.seciii.prism063.core.mapper.NewsMapper;
import com.seciii.prism063.core.service.impl.NewsServiceImpl;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class NewsServiceImplTests {
    @Mock
    private NewsMapper newsMapperMock;
    @InjectMocks
    private NewsServiceImpl newsService;
    //TODO: implement tests
}
