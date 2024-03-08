package com.seciii.prism063.common.enums;

import lombok.Getter;

/**
 * 类型枚举类，迭代二将根据需求重构
 * @author wangmingsong
 */
@Getter
public enum CategoryType {
    /**
     * 新闻分类
     */
    POLITICAL("政治","politics"),
    ECONOMY("经济","economy"),
    STOCKING("股票","stocking"),
    MILITARY("军事","military"),
    EDUCATION("教育","education"),
    TECHNOLOGY("科技","technology"),
    CULTURE("文化","culture"),
    SPORTS("体育","sports"),
    ENTERTAINMENT("娱乐","entertainment"),
    HEALTH("健康","health"),
    OTHER("其他","other");

    private final String categoryCN;
    private final String categoryEN;

    CategoryType(String categoryCN,String categoryEN) {
        this.categoryCN=categoryCN;
        this.categoryEN=categoryEN;
    }

    public int toInt(){
        return this.ordinal();
    }
    public boolean equals(int ord){
        return this.ordinal()==ord;
    }
    public static CategoryType getCategoryType(String category){
        for(CategoryType categoryType:CategoryType.values()){
            if(categoryType.getCategoryCN().equals(category)||categoryType.getCategoryEN().equals(category)){
                return categoryType;
            }
        }
        return CategoryType.OTHER;
    }
    public static CategoryType getCategoryType(int category){
        for(CategoryType categoryType:CategoryType.values()){
            if(categoryType.ordinal()==category){
                return categoryType;
            }
        }
        return CategoryType.OTHER;
    }
}
