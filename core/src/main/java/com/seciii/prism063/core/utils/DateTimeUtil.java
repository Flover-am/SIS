package com.seciii.prism063.core.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期时间工具类
 * @author wang mingsong
 * @date 2024.03.11
 */
public class DateTimeUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static LocalDateTime defaultParse(String date) {
        if(date==null|| date.isEmpty()){
            return null;
        }
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
    public static String defaultFormat(LocalDateTime date) {
        if(date==null){
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
