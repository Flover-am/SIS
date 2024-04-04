package com.seciii.prism030.core.pojo.vo.news;

import jdk.dynalink.linker.LinkerServices;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NewsDateCountVo {
    private String date;
    private List<NewsCategoryCountVO> newsCategoryCounts;
}
