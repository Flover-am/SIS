package com.seciii.prism030.core.utils;

import com.seciii.prism030.core.enums.CategoryType;

import java.util.ArrayList;
import java.util.List;

public class RedisKeyUtil {
    //----------------------------------------key------------------------------------------------//
    public static String sourceKey(String date) {
        return "newsDate:" + date + ":sources";
    }


    @Deprecated
    public static String categoryKey(String date, int category) {
        return "newsDate:" + date + ":category:" + category;
    }

    @Deprecated
    public static String dayKey(String date) {
        return "newsDate:" + date;
    }

    public static String categoryCountKey(String date, int category) {
        return "newsDate:" + date + ":category:" + category + ":count";
    }


    public static List<String> categoriesCountKey(String date) {
        // 0-13
        List<String> res = new ArrayList<>();
        for (int i = 0; i < CategoryType.values().length; i++) {
            res.add(categoryCountKey(date, i));
        }
        return res;

    }


    public static String dayCountKey(String date) {
        return "newsDate:" + date + ":count";
    }
}
