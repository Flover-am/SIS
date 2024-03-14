package com.seciii.prism030.core.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期时间工具类
 *
 * @author wang mingsong
 * @date 2024.03.11
 */
public class DateTimeUtil {
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String BEGIN_TIME_OF_DAY = "00:00:00";
    private static final String END_TIME_OF_DAY = "23:59:59";

    private DateTimeUtil() {}

    /**
     * 默认解析日期时间，格式为yyyy-MM-dd HH:mm:ss
     *
     * @param date 日期时间字符串
     * @return 解析后的LocalDateTime对象
     */
    public static LocalDateTime defaultParse(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DEFAULT_FORMAT));
    }

    /**
     * 默认格式化日期时间，格式为yyyy-MM-dd HH:mm:ss
     *
     * @param date 日期时间
     * @return 格式化后的日期时间字符串
     */
    public static String defaultFormat(LocalDateTime date) {
        if (date == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern(DEFAULT_FORMAT));
    }

    public static LocalDateTime parseBeginOfDay(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(date + " " + BEGIN_TIME_OF_DAY, DateTimeFormatter.ofPattern(DEFAULT_FORMAT));
    }

    public static LocalDateTime parseEndOfDay(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(date + " " + END_TIME_OF_DAY, DateTimeFormatter.ofPattern(DEFAULT_FORMAT));
    }

}
