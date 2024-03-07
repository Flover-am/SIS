package com.seciii.prism063.core.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Filter {
    List<String> category;
    DateRange dateRange;
}
