package com.seciii.prism063.core.pojo.vo.news;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Filter {
    List<String> category;
    String startDate;
    String endDate;
    String originSource;
}
