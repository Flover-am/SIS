package com.seciii.prism030.core.pojo.vo.news;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewsCategoryCountVO {
    private String category;
    private Integer count;

}
