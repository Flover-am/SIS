package com.seciii.prism030.core.decorator.classifier;

import java.nio.file.FileSystems;

/**
 * 分类器常量类
 *
 * @author wang mingsong
 * @date 2024.03.28
 */
public class ClassifierConstants {
    private ClassifierConstants() {
    }

    public static final String MODEL_PATH = "news_model"; /* 新闻分类模型路径名 */

    public static final String LEXICON = "lexicon"; /* 字典文件名 */

    public static final String MODEL = "model"; /* 模型文件名 */
    public static final String CATEGORY = "category"; /* 分类路径名 */

    public static final String SYS_TEMP_PATH = System.getProperty("java.io.tmpdir"); /* 临时文件路径 */

    public static final String SEPARATOR = FileSystems.getDefault().getSeparator(); /* 文件分隔符 */
}
