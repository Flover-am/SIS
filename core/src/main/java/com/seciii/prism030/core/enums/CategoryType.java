package com.seciii.prism030.core.enums;


import lombok.Getter;

/**
 * 类型枚举类，迭代二将根据需求重构
 *
 * @author wangmingsong
 * @date 2024.03.01
 */
@Getter
public enum CategoryType {
    /**
     * 新闻分类
     */
    FINANCE("财经", "finance"),
    EDUCATION("教育", "education"),
    REAL_ESTATE("房产", "real_estate"),
    ASTROLOGY("星座", "astrology"),
    TECHNOLOGY("科技", "technology"),
    FASHION("时尚", "fashion"),
    LOTTERY("彩票", "lottery"),
    SPORTS("体育", "sports"),
    GAME("游戏", "game"),
    POLITICS("时政", "politics"),
    STOCK("股票", "stock"),
    ENTERTAINMENT("娱乐", "entertainment"),

    SOCIETY("社会", "society"),
    HOUSEHOLD("家居", "household"),
    OTHER("其他", "other");

    private final String categoryCN;
    private final String categoryEN;

    CategoryType(String categoryCN, String categoryEN) {
        this.categoryCN = categoryCN;
        this.categoryEN = categoryEN;
    }

    public int toInt() {
        return this.ordinal();
    }

    public boolean equals(int ord) {
        return this.ordinal() == ord;
    }

    public boolean equals(String category) {
        return this.categoryCN.equals(category) || this.categoryEN.equals(category);
    }

    public String toString() {
        return this.categoryEN;
    }

    /**
     * 从下标获取新闻类型
     * @param category 类型下标
     * @return 新闻类型
     */
    public static CategoryType of(int category){
        return CategoryType.getCategoryType(category);
    }

    /**
     * 从字符串获取新闻类型
     * @param category 类型字符串
     * @return 新闻类型
     */
    public static CategoryType of(String category){
        return CategoryType.getCategoryType(category);
    }

    /**
     * 从字符串获取新闻类型
     *
     * @param category 类型字符串
     * @return 新闻类型
     */
    public static CategoryType getCategoryType(String category) {
        for (CategoryType categoryType : CategoryType.values()) {
            if (categoryType.getCategoryCN().equals(category) || categoryType.getCategoryEN().equals(category)) {
                return categoryType;
            }
        }
        return CategoryType.OTHER;
    }

    /**
     * 从新闻类型下标获取新闻类型
     *
     * @param category 类型下标
     * @return 新闻类型
     */
    public static CategoryType getCategoryType(int category) {
        for (CategoryType categoryType : CategoryType.values()) {
            if (categoryType.ordinal() == category) {
                return categoryType;
            }
        }
        return CategoryType.OTHER;
    }
}
