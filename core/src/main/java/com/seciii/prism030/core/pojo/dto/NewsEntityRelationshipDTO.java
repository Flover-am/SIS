package com.seciii.prism030.core.pojo.dto;


import lombok.Builder;
import lombok.Data;

/**
 * 新闻实体关系dto
 *
 * @author xueruichen
 * @date 2024.04.09
 */
@Data
@Builder
public class NewsEntityRelationshipDTO {
    /**
     * 实体1
     */
    private String entity1;

    /**
     * 关系
     */
    private String relationship;

    /**
     * 实体2
     */
    private String entity2;
}
