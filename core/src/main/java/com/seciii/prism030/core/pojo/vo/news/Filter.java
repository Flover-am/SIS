package com.seciii.prism030.core.pojo.vo.news;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 筛选新闻所用的过滤条件
 *
 * @author wangmingsong
 * @date 2024.03.12
 */
@Data
@AllArgsConstructor
public class Filter {
    /**
     * 新闻类别数组
     */
    List<String> category;
    /**
     * 开始时间
     */
    String startDate;
    /**
     * 截止时间
     */
    String endDate;
    /**
     * 新闻来源
     */
    String originSource;
}
