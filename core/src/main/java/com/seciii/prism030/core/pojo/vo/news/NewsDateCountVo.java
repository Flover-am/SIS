package com.seciii.prism030.core.pojo.vo.news;

import jdk.dynalink.linker.LinkerServices;
import lombok.Builder;
import lombok.Data;

import java.util.List;
/**
 * @author : LiDongSheng
 * @date : 2024.4.15
 * 新闻日期统计VO
 */
@Data
@Builder
public class NewsDateCountVo {
    private String date;
    private List<NewsCategoryCountVO> newsCategoryCounts;
}
