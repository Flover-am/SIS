package com.seciii.prism030.core.decorator.segment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class SegmentTest {
    @Autowired
    TextSegment textSegment;
    @Test
    void segmentTest() {
        String text="小明和小红是使用seciii的好朋友";
        String[] res=textSegment.segment(text);
        System.out.println(Arrays.toString(res));
    }
}
