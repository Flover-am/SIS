package com.seciii.prism063.core.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期时间工具类
 *
 * @author wang mingsong
 * @date 2024.03.11
 */
public class DateTimeUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

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
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
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
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
