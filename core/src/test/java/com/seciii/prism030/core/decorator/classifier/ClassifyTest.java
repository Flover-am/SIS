package com.seciii.prism030.core.decorator.classifier;

import com.seciii.prism030.core.enums.CategoryType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ClassifyTest {
    @Autowired
    Classifier classifier;

    @Test
    public void test() {
        String text = "科比坠机";
        classifier.topNClassify(text, 3).forEach(System.out::println);
        assertEquals(CategoryType.SPORTS, classifier.classify(text));
    }
}
