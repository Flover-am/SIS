package com.seciii.prism063.core.enums;


/**
 * 类型枚举类，迭代二将根据需求重构
 * @author wangmingsong
 * @date 2024.03.01
 */
public enum CategoryType {
    /**
     * 新闻分类
     */
    DOMESTIC("国内","domestic"),
    INTERNATIONAL("国际","international"),
    SOCIETY("社会","society"),
    SPORTS("体育","sports"),
    ENTERTAINMENT("娱乐","entertainment"),
    TECHNOLOGY("科技","technology"),
    FINANCE("财经","finance"),
    STOCK("股票","stock"),
    AMERICAN_STOCK("美股","american stock"),
    OTHER("其他","other");

    private final String categoryCN;
    private final String categoryEN;

    CategoryType(String categoryCN,String categoryEN) {
        this.categoryCN=categoryCN;
        this.categoryEN=categoryEN;
    }
    public String getCategoryCN(){
        return this.categoryCN;
    }
    public String getCategoryEN(){
        return this.categoryEN;
    }
    public int toInt(){
        return this.ordinal();
    }
    public boolean equals(int ord){
        return this.ordinal()==ord;
    }
    public boolean equals(String category){
        return this.categoryCN.equals(category)||this.categoryEN.equals(category);
    }
    public String toString(){
        return this.categoryEN;
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
