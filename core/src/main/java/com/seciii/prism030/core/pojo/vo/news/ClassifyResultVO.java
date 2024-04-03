package com.seciii.prism030.core.pojo.vo.news;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 分类结果VO
 * @author wang mingsong
 * @date 2024.03.28
 */
@Getter
@Setter
@AllArgsConstructor
public class ClassifyResultVO {
    private String category;
    private double probability;
}
