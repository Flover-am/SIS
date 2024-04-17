package com.seciii.prism030.core.decorator.segment;

/**
 * 分词模块常量类
 *
 * @author wang mingsong
 * @date 2024.04.14
 */
public class SegmentConstants {
    private SegmentConstants() {
    }

    /* 分词服务前缀 */
    public final static String urlAffix = "/v2";
    /* 分词服务方法 */
    public final static String segMethod = "/segment";
    /* 分词+实体识别方法 */
    public final static String lacMethod = "/lac";
    /* 分词+实体识别+语义排名方法 */
    public final static String rankMethod = "/rank";


}
