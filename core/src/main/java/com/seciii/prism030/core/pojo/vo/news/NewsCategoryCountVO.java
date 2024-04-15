package com.seciii.prism030.core.pojo.vo.news;

import lombok.Builder;
import lombok.Data;

/**
 * 新闻分类数量VO
 * @author LiDongSheng
 * @date 2024.4.15
 */
@Data
@Builder
public class NewsCategoryCountVO {

    private String category;
    private Integer count;

}
