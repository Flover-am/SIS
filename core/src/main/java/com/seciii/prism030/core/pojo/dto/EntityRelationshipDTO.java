package com.seciii.prism030.core.pojo.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 实体关系类
 *
 * @author wang mingsong
 * @date 2024.05.14
 */
@Builder
@Getter
public class EntityRelationshipDTO {
    private final String fromEntity;
    private final String relationship;
    private final String toRelationship;
}
