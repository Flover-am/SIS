package com.seciii.prism030.core.pojo.vo.news;


import lombok.Builder;
import lombok.Data;

/**
 * 时间轴VO类
 *
 * @author xueruichen
 * @date 2024.06.06
 */
@Data
@Builder
public class TimelineUnitVO {
    /**
     * 事件描述
     */
    private String event;

    /**
     * 事件发生时间
     */
    private String time;

    /**
     * 新闻id
     */
    private Long newsId;
}
