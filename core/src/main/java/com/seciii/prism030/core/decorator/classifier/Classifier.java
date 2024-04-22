package com.seciii.prism030.core.decorator.classifier;

import com.seciii.prism030.core.enums.CategoryType;

import static com.seciii.prism030.core.decorator.classifier.ClassifierConstants.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.thunlp.text.classifiers.BasicTextClassifier;
import org.thunlp.text.classifiers.ClassifyResult;
import org.thunlp.text.classifiers.LinearBigramChineseTextClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于THUCTC项目的文本分类器
 *
 * @author wangmingsong
 * @date 2024.03.28
 */
@Component
@Slf4j
public class Classifier {
    private BasicTextClassifier classifier = null;

    public Classifier() {
        setClassifier();
    }

    /**
     * 返回新闻的分类结果
     *
     * @param text 新闻
     * @return 分类结果
     */
    public CategoryType classify(String text) {
        return topNClassify(text, 1).get(0).getFirst();
    }

    /**
     * 返回新闻的topN分类结果
     *
     * @param text 新闻
     * @param topN topN个数
     * @return topN分类结果
     */
    public List<Pair<CategoryType, Double>> topNClassify(String text, int topN) {

        //防止N超过新闻类别个数
        if (topN > CategoryType.values().length - 1) {
            topN = CategoryType.values().length - 1;
        }
        setClassifier();

        List<Pair<CategoryType, Double>> resultList = new ArrayList<>();

        ClassifyResult[] result = classifier.classifyText(text, topN);
        for (int i = 0; i < topN; ++i) {
            resultList.add(Pair.of(CategoryType.of(classifier.getCategoryName(result[i].label)), result[i].prob));
        }
        return resultList;
    }

    /**
     * 设置分类器
     */
    private void setClassifier() {
        if (this.classifier == null) {
            this.classifier = new BasicTextClassifier();
            File modelFile = getFileFromResource(MODEL);
            System.out.println();
            if (modelFile == null) {
                log.error("Model loading Failed!");
                throw new RuntimeException("Model loading Failed!");
            }
            File categoryFile = getFileFromResource(CATEGORY);
            if (categoryFile == null) {
                log.error("Category loading Failed!");
                throw new RuntimeException("Category loading Failed!");
            }
            File lexiconFile = getFileFromResource(LEXICON);
            if (lexiconFile == null) {
                log.error("Lexicon loading Failed!");
                throw new RuntimeException("Lexicon loading Failed!");
            }
            // 设置分类种类，并读取模型
            classifier.loadCategoryListFromFile(categoryFile.getAbsolutePath());
            classifier.setTextClassifier(new LinearBigramChineseTextClassifier(classifier.getCategorySize()));
            classifier.getTextClassifier().loadModel(lexiconFile, modelFile);
        }
    }

    private File getFileFromResource(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(MODEL_PATH+SEPARATOR+fileName);
        ) {
            File tmpFile = Files.createTempFile(fileName, ".tmp").toFile();
            try (OutputStream outputStream = new FileOutputStream(tmpFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            inputStream.close();
            return tmpFile;
        } catch (Exception e) {
            log.error("File {} not found: {}", fileName, e.getMessage());
            return null;
        }
    }

}
