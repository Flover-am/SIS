package com.seciii.prism030.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 可信度等级
 *
 * @author xueruichen
 * @date 2024.06.07
 */
@Getter
@AllArgsConstructor
public enum ReliabilityLevel {
    VERY_LOW("非常低"),

    LOW("比较低"),

    MODERATE("中等"),

    HIGH("比较高"),

    VERY_HIGH("非常高");

    /**
     * 可信度等级
     */
    private final String level;
}
