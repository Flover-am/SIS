package com.seciii.prism030.core.pojo.vo.llm;

import lombok.Builder;
import lombok.Data;

/**
 * 可信度VO类
 *
 * @author xueruichen
 * @date 2024.06.07
 */
@Data
@Builder
public class ReliabilityVO {
    /**
     * 可信度
     */
    private Integer reliability;

    /**
     * 理由
     */
    private String reason;
}
