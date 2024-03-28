package com.seciii.prism030.core.classifier;

import com.seciii.prism030.core.enums.CategoryType;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.thunlp.text.classifiers.BasicTextClassifier;
import org.thunlp.text.classifiers.ClassifyResult;
import org.thunlp.text.classifiers.LinearBigramChineseTextClassifier;

import java.util.ArrayList;
import java.util.List;
/**
 * 基于THUCTC项目的文本分类器
 *
 * @author wangmingsong
 * @date 2024.03.28
 */
@Component
public class Classifier {
    private BasicTextClassifier classifier=null;
    public Classifier(){
        setClassifier();
    }
    /**
     * 返回新闻的分类结果
     * @param text 新闻
     * @return 分类结果
     */
    public CategoryType classify(String text){
        return topNClassify(text,1).get(0).getFirst();
    }

    /**
     * 返回新闻的topN分类结果
     * @param text 新闻
     * @param topN topN个数
     * @return topN分类结果
     */
    public List<Pair<CategoryType,Double>> topNClassify(String text, int topN){

        //防止N超过新闻类别个数
        if(topN>CategoryType.values().length-1){
            topN=CategoryType.values().length-1;
        }
        setClassifier();

        List<Pair<CategoryType,Double>> resultList= new ArrayList<>();

        ClassifyResult[] result = classifier.classifyText(text, topN);
        for (int i = 0; i < topN; ++i) {
            resultList.add(Pair.of(CategoryType.of(classifier.getCategoryName(result[i].label)),result[i].prob));
        }
        return resultList;
    }

    /**
     * 设置分类器
     */
    private void setClassifier(){
        if(this.classifier==null){
            this.classifier = new BasicTextClassifier();
            String modelPath=Classifier.class.getClassLoader().getResource(ClassifierConstants.MODEL_PATH).getPath();

            // 设置分类种类，并读取模型
            classifier.loadCategoryListFromFile(modelPath+'/'+ClassifierConstants.CATEGORY_PATH);
            classifier.setTextClassifier(new LinearBigramChineseTextClassifier(classifier.getCategorySize()));
            classifier.getTextClassifier().loadModel(modelPath);
        }
    }

}
