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
    NBA("NBA", "NBA"),
    CBA("CBA", "CBA"),
    CSL("中超", "CSL"),
    AS("亚冠", "AS"),
    PL("英超", "PL"),
    CL("欧冠", "CL"),
    LA_LIGA("西甲", "LaLiga"),
    BUNDESLIGA("德甲", "Bundesliga"),
    UEFA("欧联", "UEFA"),
    CNFB("国足", "CNFB"),
    BADMINTON("羽毛球", "Badminton"),
    BILLIARD("台球", "Billiard"),
    VOLLEYBALL("排球", "Volleyball"),
    SWIM_DIVE("游泳/跳水", "Swim/Dive"),
    PINGPONG("乒乓球", "PingPong"),
    ATHLETICS("田径", "Athletics"),
    OTHER("其他", "Other");

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
     *
     * @param category 类型下标
     * @return 新闻类型
     */
    public static CategoryType of(int category) {
        return CategoryType.getCategoryType(category);
    }

    /**
     * 从字符串获取新闻类型
     *
     * @param category 类型字符串
     * @return 新闻类型
     */
    public static CategoryType of(String category) {
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
        return null;
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
        return null;
    }
}
